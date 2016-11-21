package org.roylance.yadel.api.actors

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.api.models.ConfigurationActorRef
import org.roylance.yadel.api.services.file.FileDagStore
import org.roylance.yadel.api.services.memory.MemoryDagStore
import org.roylance.yadel.api.utilities.ActorUtilities
import java.util.*

abstract class ManagerBase: UntypedActor() {
    protected val workers = HashMap<String, ConfigurationActorRef>()
    protected val activeDags = MemoryDagStore()
    protected val savedDags = FileDagStore(ActorUtilities.CommonDagFile)
    protected val unprocessedDags = FileDagStore(ActorUtilities.UnprocessedDags)

    protected val reportActor = this.context().system().actorOf(Props.create(ReportActor::class.java))!!
    protected val log: LoggingAdapter
        get() = Logging.getLogger(this.context().system(), this)

    override fun preStart() {
        println(ActorUtilities.buildMemoryLogMessage())

        super.preStart()
        val runnable = Runnable {
            this.self.tell(YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING, this.self)
        }
        this.context.system().scheduler().schedule(ActorUtilities.OneMinute, ActorUtilities.OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(message: Any?) {
        // is this a dag from someone?
        if (message is YadelModel.WorkerState && message == YadelModel.WorkerState.IDLE) {
            updateSenderStatus(YadelModel.WorkerState.IDLE, true)
        }
        else if (message is YadelModel.Dag) {
            if (activeDags.size() > ActorUtilities.MaxActiveDags) {
                unprocessedDags.set(message.id, message.toBuilder())
            }
            else {
                activeDags.set(message.id, message.toBuilder())
            }
        }
        // not going to try to add tasks to an unprocessed dag
        else if (message is YadelModel.AddTaskToDag &&
                message.hasNewTask() &&
                activeDags.containsKey(message.newTask.dagId)) {
            this.log.info("looking for ${message.newTask.dagId}")
            val foundDag = this.activeDags.get(message.newTask.dagId)!!
            foundDag.addUncompletedTasks(message.newTask).addFlattenedTasks(message.newTask)
            this.log.info("saved task to dag")
            activeDags.set(foundDag.id, foundDag)
        }
        else if (message is YadelModel.CompleteTask && this.activeDags.containsKey(message.task.dagId)) {
            updateSenderStatus(YadelModel.WorkerState.IDLE)

            val foundDag = this.activeDags.get(message.task.dagId)!!
            val existingTask = foundDag.processingTasksBuilderList.firstOrNull { it.id == message.task.id }
            if (existingTask != null) {
                val indexToRemove = foundDag.processingTasksBuilderList.indexOf(existingTask)
                foundDag.removeProcessingTasks(indexToRemove)
            }
            else {
                this.log.info("could not find ${message.task.id} in the processing tasks, for some reason...")
            }

            if (message.isError) {
                foundDag.addErroredTasks(message.task)
            }
            else {
                foundDag.addCompletedTasks(message.task)
            }

            if (foundDag.uncompletedTasksCount == 0 &&
                foundDag.processingTasksCount == 0) {
                activeDags.remove(foundDag.id)
                savedDags.set(foundDag.id, foundDag)

                val currentUnprocessedDags = unprocessedDags.values()
                if (currentUnprocessedDags.isNotEmpty()) {
                    val unprocessedDagToUse = currentUnprocessedDags.first()
                    unprocessedDags.remove(unprocessedDagToUse.id)
                    activeDags.set(unprocessedDagToUse.id, unprocessedDagToUse)
                }
            }
            else {
                activeDags.set(foundDag.id, foundDag)
            }
        }
        else if (message is YadelModel.WorkerToManagerMessageType &&
                YadelModel.WorkerToManagerMessageType.REGISTRATION == message) {
            this.log.info("handling registration")
            this.handleRegistration()
        }
        else if (message is Terminated) {
            this.log.info("handling termination")
            this.handleTermination(message)
        }
        else if (message is YadelReport.UIYadelRequest) {
            if (message.requestType == YadelReport.UIYadelRequestType.REPORT_DAGS) {
                this.handleReport()
            }
            if (message.requestType == YadelReport.UIYadelRequestType.DELETE_DAG) {
                this.log.info("attempting to remove ${message.dagId}")
                if (activeDags.containsKey(message.dagId)) {
                    activeDags.remove(message.dagId)
                }
                else if (savedDags.containsKey(message.dagId)) {
                    savedDags.remove(message.dagId)
                }
            }
            if (message.requestType == YadelReport.UIYadelRequestType.GET_DAG_STATUS) {
                if (activeDags.containsKey(message.dagId)) {
                    val uiDag = ActorUtilities.buildUIDagFromDag(activeDags.get(message.dagId)!!)
                    sender.tell(uiDag.build(), self)
                }
                else if (savedDags.containsKey(message.dagId)) {
                    val uiDag = ActorUtilities.buildUIDagFromDag(savedDags.get(message.dagId)!!)
                    sender.tell(uiDag.build(), self)
                }
            }
        }
        else if (message is YadelReport.UIYadelRequestType &&
                YadelReport.UIYadelRequestType.REPORT_DAGS == message) {
            handleReport()
        }
        else if (message is YadelModel.ManagerToManagerMessageType &&
                YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING == message) {
            log.info("Have ${this.activeDags.values().size} active dags with ${this.workers.size} workers:")
            log.info("with ${unprocessedDags.size()} unprocessed dags")
            log.info("with ${savedDags.size()} saved dags")
            log.info(ActorUtilities.buildMemoryLogMessage())

            activeDags.values().forEach { actualDag ->
                val availableTaskIds = ActorUtilities.getAllAvailableTaskIds(actualDag)
                this.log.info("${actualDag.display} (${actualDag.id})")
                this.log.info("${actualDag.uncompletedTasksCount} uncompleted tasks")
                this.log.info("${actualDag.processingTasksCount} processing tasks")
                this.log.info("${actualDag.erroredTasksCount} error tasks")
                this.log.info("${actualDag.completedTasksCount} completed tasks")

                this.log.info("${availableTaskIds.size} task(s) can be executed right now with ${this.workers.values.count { it.configuration.state == YadelModel.WorkerState.IDLE }} idle worker(s)")
            }
            this.workers.values.forEach {
                this.log.info("${it.actorRef.path()}: ${it.configuration.state.name}")
            }
        }

        this.tellWorkersToDoNewDagWork()
        this.freeAnyWorkersRunningOverAllowedAmount()
    }

    protected fun tellWorkersToDoNewDagWork() {
        if (this.workers.values.filter { it.configuration.state == YadelModel.WorkerState.IDLE }.isEmpty()) {
            return
        }

        // move unprocessed to active, if exists...
        if (activeDags.size() < ActorUtilities.MaxActiveDags &&
            unprocessedDags.size() > 0) {
            val firstUnprocessedDag = unprocessedDags.values().first()
            unprocessedDags.remove(firstUnprocessedDag.id)
            activeDags.set(firstUnprocessedDag.id, firstUnprocessedDag)
        }

        activeDags.values().forEach { foundActiveDag ->
            if (!ActorUtilities.canProcessDag(foundActiveDag, activeDags)) {
                return@forEach
            }

            val taskIdsToProcess = ActorUtilities.getAllAvailableTaskIds(foundActiveDag)
            taskIdsToProcess.forEach { uncompletedTaskId ->
                val openWorker = getOpenWorker()
                if (openWorker != null) {
                    val workerKey = getActorRefKey(openWorker.actorRef)
                    val foundTuple = workers[workerKey]
                    val worker = foundTuple!!.configuration.toBuilder()
                    worker.state = YadelModel.WorkerState.WORKING

                    val task = foundActiveDag.uncompletedTasksBuilderList.first { it.id == uncompletedTaskId }
                    val taskIdx = foundActiveDag.uncompletedTasksBuilderList.indexOf(task!!)
                    foundActiveDag.removeUncompletedTasks(taskIdx)

                    val updatedTask = task.setExecutionDate(Date().time).setStartDate(Date().time)
                    foundActiveDag.addProcessingTasks(updatedTask.build())

                    worker.task = updatedTask.build()
                    worker.dag = foundActiveDag.build()
                    worker.taskStartTime = LocalDateTime.now().toString()

                    workers[workerKey] = ConfigurationActorRef(openWorker.actorRef, worker.build())
                    openWorker.actorRef.tell(task.build(), self)
                }
            }
        }
    }

    private fun handleReport() {
        val allDags = YadelModel.AllDags.newBuilder()

        workers.values.forEach {
            allDags.addWorkers(it.configuration)
        }

        activeDags.values().forEach {
            allDags.addDags(it)
        }

        reportActor.forward(allDags.build(), context)
    }

    private fun updateSenderStatus(newState:YadelModel.WorkerState, setTaskToUnprocessed: Boolean = false) {
        val workerKey = this.getActorRefKey(sender)
        if (workers.containsKey(workerKey)) {
            log.info("setting $workerKey to ${newState.name}")
            val foundTuple = workers[workerKey]
            val foundWorker = foundTuple!!.configuration.toBuilder()
            foundWorker.state = newState

            if (setTaskToUnprocessed &&
                    foundWorker.hasDag() &&
                    foundWorker.hasTask() &&
                    activeDags.containsKey(foundWorker.dag.id)) {
                val foundDag = activeDags.get(foundWorker.dag.id)!!

                val foundTask = foundDag.processingTasksList.firstOrNull { it.id == foundWorker.task.id }
                if (foundTask != null) {
                    log.info("setting ${foundTask.display} back to unprocessed")
                    val foundTaskIdx = foundDag.processingTasksList.indexOf(foundTask)
                    foundDag.removeProcessingTasks(foundTaskIdx)
                    foundDag.addUncompletedTasks(foundTask)
                }

                activeDags.set(foundDag.id, foundDag)
            }

            foundWorker.clearDag()
            foundWorker.clearTask()
            foundWorker.clearTaskStartTime()

            workers[workerKey] = ConfigurationActorRef(sender, foundWorker.build())
        }
    }

    protected fun getOpenWorker():ConfigurationActorRef? {
        val openWorkers = this.workers.values.filter { it.configuration.state == YadelModel.WorkerState.IDLE }
        if (openWorkers.isEmpty()) {
            return null
        }
        return openWorkers.first()
    }

    private fun freeAnyWorkersRunningOverAllowedAmount() {
        val now = LocalDateTime.now()

        val workersToReset = HashSet<String>()
        this.workers.keys.forEach {
            val worker = this.workers[it]!!

            if (worker.configuration.state == YadelModel.WorkerState.WORKING) {
                val taskStartTime = LocalDateTime.parse(worker.configuration.taskStartTime)

                val minutes = Minutes.minutesBetween(taskStartTime, now).minutes
                if (minutes > MinutesBeforeTaskReset) {
                    if (this.activeDags.containsKey(worker.configuration.dag.id)) {
                        val activeDag = this.activeDags.get(worker.configuration.dag.id)!!
                        val foundTask = activeDag.processingTasksList.firstOrNull { it.id == worker.configuration.task.id }

                        if (foundTask != null) {
                            if (!foundTask.isWaitingForAnotherDagTask) {
                                workersToReset.add(it)
                                val foundTaskIdx = activeDag.processingTasksList.indexOf(foundTask)

                                activeDag.removeProcessingTasks(foundTaskIdx)
                                activeDag.addUncompletedTasks(foundTask)
                            }
                        }
                    }
                }
            }
        }

        workersToReset.forEach {
            val foundWorker = this.workers[it]!!

            val tempConfiguration = foundWorker.configuration.toBuilder()
            tempConfiguration.state = YadelModel.WorkerState.IDLE
            tempConfiguration.clearDag()
            tempConfiguration.clearTask()
            tempConfiguration.clearTaskStartTime()

            this.workers[it] = ConfigurationActorRef(foundWorker.actorRef, tempConfiguration.build())
        }
    }

    private fun handleRegistration() {
        this.context.watch(this.sender)
        val key = this.getActorRefKey(this.sender)

        if (workers.containsKey(key)) {
            // going to reset the task status, if there was one
            updateSenderStatus(YadelModel.WorkerState.IDLE, true)
        }

        // new configuration and reference
        val newConfiguration = YadelModel.WorkerConfiguration.newBuilder()
                .setHost(this.sender.path().address().host().get())
                .setPort(this.sender.path().address().port().get().toString())
                .setIp(key)
                .setInitializedTime(LocalDateTime.now().toString())
                .setMinutesBeforeTaskReset(MinutesBeforeTaskReset)
                .setState(YadelModel.WorkerState.IDLE)
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
        private val MinutesBeforeTaskReset = 20L
    }
}
