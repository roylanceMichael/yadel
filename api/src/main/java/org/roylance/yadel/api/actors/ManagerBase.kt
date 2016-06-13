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

open class ManagerBase :UntypedActor() {
    protected val workers = HashMap<String, ConfigurationActorRef>()
    protected val messagesInQueue = ArrayList<YadelModels.ServiceToManagerMessage>()

    protected val log = Logging.getLogger(this.context().system(), this)

    override fun preStart() {
        val runnable = Runnable {
            this.log.info("Have ${messagesInQueue.size} waiting with ${this.workers.size} workers:")
            this.workers.values.forEach {
                this.log.info("${it.actorRef.path().toString()}: ${it.configuration.state.name}")
            }
            this.self.tell(YadelModels.ServiceToManagerMessage.newBuilder().setType(YadelModels.ServiceToManagerType.GET_IDLE_WORKERS_WORKING).build(), this.self)
        }
        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
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
                this.handleWorkerMessage(p0, YadelModels.WorkerState.IDLE)
                if (this.messagesInQueue.size > 0) {
                    val firstMessage = this.messagesInQueue.first()
                    this.messagesInQueue.remove(firstMessage)
                    this.tellWorkerToDoNewWork(firstMessage)
                }
            }
        }
        else if (p0 is YadelModels.ServiceToManagerMessage) {
            if (YadelModels.ServiceToManagerType.REPORT_WORKER_STATUS.equals(p0.type)) {
                this.log.info("reporting worker status")
                this.reportWorkerStatus()
            }
            else if (YadelModels.ServiceToManagerType.DO_NEW_WORK.equals(p0.type)) {
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
            else if (YadelModels.ServiceToManagerType.GET_IDLE_WORKERS_WORKING.equals(p0.type)) {
                this.log.info("getting any idles workers to working")
                val idleCount = this.workers.values.count { YadelModels.WorkerState.IDLE.equals(it.configuration.state) }
                if (idleCount > 0 && this.messagesInQueue.size > 0) {
                    var sentMessages = 0
                    while(sentMessages < Math.min(idleCount, this.messagesInQueue.size)) {
                        val firstMessage = this.messagesInQueue.first()
                        this.messagesInQueue.remove(firstMessage)
                        this.tellWorkerToDoNewWork(firstMessage)
                        sentMessages++
                    }
                }
            }
        }
        else if (p0 is Terminated) {
            this.log.info("handling termination")
            this.handleTermination(p0)
        }
    }

    protected fun tellWorkerToDoNewWork(message:YadelModels.ServiceToManagerMessage) {
        val openWorkers = this.workers.values.filter { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }

        if (!openWorkers.any()) {
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

    private fun reportWorkerStatus() {
        val reportModel = YadelModels.WorkerStatusReport.newBuilder()

        this.workers.forEach { s, configurationActorRef ->
            val configuration = configurationActorRef.configuration
            reportModel.addWorkerConfigurations(configuration)
        }
        val managerToServiceModel = YadelModels.ManagerToServiceMessage.newBuilder().setReport(reportModel).build()
        this.sender.tell(managerToServiceModel, this.self)
    }

    private fun handleWorkerMessage(message:YadelModels.WorkerToManagerMessage, newState:YadelModels.WorkerState) {
        val workerKey = this.getActorRefKey(this.sender)
        if (this.workers.containsKey(workerKey)) {
            val foundTuple = this.workers[workerKey]
            val newBuilder = foundTuple!!.configuration.toBuilder()
            newBuilder.state = newState
            newBuilder.lastWorkerToManagerMessage = message
            this.workers[workerKey] = ConfigurationActorRef(this.sender, newBuilder.build())
        }
    }

    private fun handleRegistration() {
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

    private fun handleTermination(terminated: Terminated) {
        val key = this.getActorRefKey(terminated.actor)
        if (this.workers.containsKey(key)) {
            this.workers.remove(key)
        }
    }

    private fun getActorRefKey(actorRef: ActorRef):String {
        return actorRef.path().address().toString()
    }

    companion object {
        private val OneMinute = Duration.create(1, TimeUnit.MINUTES);
    }
}
