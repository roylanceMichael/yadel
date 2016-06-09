package org.roylance.yadel.api.actors

import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import org.roylance.yadel.api.models.ConfigurationActorRef
import org.roylance.yadel.api.models.YadelModels
import scala.concurrent.duration.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

open class ManagerBase :UntypedActor() {
    protected val lockObject  = ReentrantLock()

    protected val workers = HashMap<String, ConfigurationActorRef>()
    protected val messagesInQueue = ArrayList<YadelModels.ServiceToManagerMessage>()

    protected val log = Logging.getLogger(this.context().system(), this)

    override fun preStart() {
        val runnable = Runnable {
            this.log.info("running queue checking on managerbase")
            this.lockObject.lock()
            val tempList = ArrayList<YadelModels.ServiceToManagerMessage>()
            try {
                this.messagesInQueue.forEach { tempList.add(it) }
                // clear it out
                this.messagesInQueue.clear()
            }
            finally {
                this.lockObject.unlock()
            }
            // try to send to workers again
            tempList.forEach {
                this.tellWorkerToDoNewWork(it)
            }
            this.log.info("done running queue checking on managerbase, have ${messagesInQueue.size} waiting still...")
        }
        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
        val messageString = p0?.toString()
        if (messageString != null && messageString.length > 100) {
            this.log.info("received message: ${messageString.substring(100)}")
        }
        else {
            this.log.info("received message: $messageString")
        }

        if (p0 is YadelModels.WorkerToManagerMessage) {
            if (YadelModels.WorkerToManagerMessageType.REGISTRATION.equals(p0.type)) {
                this.log.info("handling registration")
                this.handleRegistration()
            }
            else if (YadelModels.WorkerToManagerMessageType.NOW_WORKING.equals(p0.type)) {
                this.log.info("handling now_working")
                this.handleWorkerMessage(p0, YadelModels.WorkerState.WORKING)
            }
            else if (YadelModels.WorkerToManagerMessageType.NOW_IDLE.equals(p0.type)) {
                this.log.info("handling idle")
                this.handleWorkerMessage(p0, YadelModels.WorkerState.IDLE)
            }
        }
        else if (p0 is YadelModels.ServiceToManagerMessage) {
            if (YadelModels.ServiceToManagerType.REPORT_WORKER_STATUS.equals(p0.type)) {
                this.log.info("reporting worker status")
                this.reportWorkerStatus()
            }
            else if (YadelModels.ServiceToManagerType.DO_NEW_WORK.equals(p0.type)) {
                this.log.info("telling worker to do work")
                this.tellWorkerToDoNewWork(p0)
            }
            else if (YadelModels.ServiceToManagerType.SPAWN_NEW_WORKER.equals(p0.type)) {
                this.log.info("spawning a new worker")

                // todo: better logic around where to spawn the new worker
                if (this.workers.any()) {
                    val firstWorker = this.workers.values.first()
                    val message = YadelModels.ManagerToWorkerMessage.newBuilder()
                        .setSpawnMessage(p0.spawnMessage)
                        .setType(YadelModels.ManagerToWorkerMessageType.SPAWN_NEW_WORKER_AT_YOUR_LOCATION)
                        .build()
                    firstWorker.actorRef.tell(message, this.self)
                }
            }
            else if (YadelModels.ServiceToManagerType.REDUCE_WORKER_COUNT.equals(p0.type)) {
                this.log.info("reducing worker count")
                if (this.workers.any()) {
                    // todo: better logic about which to kill
                    val firstWorker = this.workers.values.first()

                    val message = YadelModels.ManagerToWorkerMessage.newBuilder()
                        .setType(YadelModels.ManagerToWorkerMessageType.KILL_YOURSELF)
                        .build()
                    firstWorker.actorRef.tell(message, this.sender)
                }
            }
        }
        else if (p0 is Terminated) {
            this.log.info("handling termination")
            this.handleTermination(p0)
        }
    }

    protected fun tellWorkerToDoNewWork(message:YadelModels.ServiceToManagerMessage) {
        this.lockObject.lock()
        try {
            val openWorkers = this.workers.values.filter { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }

            if (!openWorkers.any()) {
                this.log.info("did not find any open workers")
                this.messagesInQueue.add(message)
            }
            else {
                val foundWorker = openWorkers.first()
                val workerKey = this.getActorRefKey(foundWorker.actorRef)
                val foundTuple = this.workers[workerKey]
                val newBuilder = foundTuple!!.configuration.toBuilder()
                newBuilder.state = YadelModels.WorkerState.WORKING
                this.workers[workerKey] = ConfigurationActorRef(foundWorker.actorRef, newBuilder.build())

                val workMessage = YadelModels.ManagerToWorkerMessage
                        .newBuilder()
                        .setName(message.name)
                        .addAllContext(message.contextList)
                        .setType(YadelModels.ManagerToWorkerMessageType.START_WORKING)
                        .build()

                this.log.info("telling ${foundWorker.actorRef.path().address().toString()} to process ${message.name}")
                foundWorker.actorRef.tell(workMessage, this.self)
            }
        }
        finally {
            this.lockObject.unlock()
        }
    }

    private fun reportWorkerStatus() {
        this.lockObject.lock()
        try {
            val reportModel = YadelModels.WorkerStatusReport.newBuilder()

            this.workers.forEach { s, configurationActorRef ->
                val configuration = configurationActorRef.configuration
                reportModel.addWorkerConfigurations(configuration)
            }
            val managerToServiceModel = YadelModels.ManagerToServiceMessage.newBuilder().setReport(reportModel).build()
            this.sender.tell(managerToServiceModel, this.self)
        }
        finally {
            this.lockObject.unlock()
        }
    }

    private fun handleWorkerMessage(message:YadelModels.WorkerToManagerMessage, newState:YadelModels.WorkerState) {
        this.lockObject.lock()
        try {
            val workerKey = this.getActorRefKey(this.sender)
            if (this.workers.containsKey(workerKey)) {
                val foundTuple = this.workers[workerKey]
                val newBuilder = foundTuple!!.configuration.toBuilder()
                newBuilder.state = newState
                newBuilder.lastWorkerToManagerMessage = message
                this.workers[workerKey] = ConfigurationActorRef(this.sender, newBuilder.build())
            }
        }
        finally {
            this.lockObject.unlock()
        }
    }

    private fun handleRegistration() {
        this.lockObject.lock()
        try {
            this.context.watch(this.sender)
            val key = this.getActorRefKey(this.sender)

            val newConfiguration = YadelModels.WorkerConfiguration.newBuilder()
                    .setHost(this.sender.path().address().host().get())
                    .setPort(this.sender.path().address().port().get().toString())
                    .setIp(key)
                    .setInitializedTime(Date().time)
                    .setState(YadelModels.WorkerState.IDLE)
            val tuple = ConfigurationActorRef(this.sender, newConfiguration.build())
            this.workers.put(key, tuple)
        }
        finally {
            this.lockObject.unlock()
        }
    }

    private fun handleTermination(terminated: Terminated) {
        this.lockObject.lock()
        try {
            val key = this.getActorRefKey(terminated.actor)
            if (this.workers.containsKey(key)) {
                this.workers.remove(key)
            }
        }
        finally {
            this.lockObject.unlock()
        }
    }

    private fun getActorRefKey(actorRef: ActorRef):String {
        return actorRef.path().address().toString()
    }

    companion object {
        private val OneMinute = Duration.create(1, TimeUnit.MINUTES);
    }
}
