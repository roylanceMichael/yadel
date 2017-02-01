package org.roylance.yadel.services

import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.models.ConfigurationActorRef
import org.roylance.yadel.services.file.FileDagStore
import org.roylance.yadel.utilities.ActorUtilities
import java.util.*

class ManagerService(
        private val includeSavedDagsInReport: Boolean,
        private val savedDags: IDagStore,
        private val activeDags: IDagStore,
        private val unprocessedDags: IDagStore,
        private val workers: MutableMap<String, ConfigurationActorRef>,
        private val reportActor: IActor,
        private val log: ITaskLogger,
        private val minutesBeforeTaskReset: Long = 20L): IManagerService {

    override fun getWorkers(): Map<String, ConfigurationActorRef> {
        return workers
    }

    override fun getIncludeSavedDagsInReport(): Boolean {
        return includeSavedDagsInReport
    }

    override fun getSavedDags(): IDagStore {
        return savedDags
    }

    override fun activeDags(): IDagStore {
        return activeDags
    }

    override fun getUnprocessedDags(): IDagStore {
        return unprocessedDags
    }

    override fun handleAddTaskToDag(addTaskToDag: YadelModel.AddTaskToDag) {
        if (!activeDags.containsKey(addTaskToDag.newTask.dagId)) {
            return
        }

        this.log.info("looking for ${addTaskToDag.newTask.dagId}")
        val foundDag = this.activeDags.get(addTaskToDag.newTask.dagId)!!
        foundDag.addUncompletedTasks(addTaskToDag.newTask).addFlattenedTasks(addTaskToDag.newTask)
        this.log.info("saved task to dag")
        activeDags.set(foundDag.id, foundDag)
    }

    override fun handleCompleteTask(completeTask: YadelModel.CompleteTask, sender: IActor) {
        if (!activeDags.containsKey(completeTask.task.dagId)) {
            return
        }

        updateSenderStatus(YadelModel.WorkerState.IDLE, false, sender)

        val foundDag = this.activeDags.get(completeTask.task.dagId)!!
        val existingTask = foundDag.processingTasksBuilderList.firstOrNull { it.id == completeTask.task.id }
        if (existingTask != null) {
            val indexToRemove = foundDag.processingTasksBuilderList.indexOf(existingTask)
            foundDag.removeProcessingTasks(indexToRemove)
        }
        else {
            this.log.info("could not find ${completeTask.task.id} in the processing tasks, for some reason...")
        }

        if (completeTask.isError) {
            foundDag.addErroredTasks(completeTask.task)

            activeDags.remove(foundDag.id)
            savedDags.set(foundDag.id, foundDag)
        }
        else {
            foundDag.addCompletedTasks(completeTask.task)

            if (foundDag.uncompletedTasksCount == 0 &&
                    foundDag.processingTasksCount == 0) {
                activeDags.remove(foundDag.id)
                savedDags.set(foundDag.id, foundDag)
            }
            else {
                activeDags.set(foundDag.id, foundDag)
            }
        }
    }

    override fun handleReport(includeUnprocessed: Boolean) {
        val allDags = YadelModel.AllDags.newBuilder()
                .setIncludeUnprocessed(includeUnprocessed)
                .setIncludeFileSaved(includeSavedDagsInReport && savedDags is FileDagStore)

        workers.values.forEach {
            allDags.addWorkers(it.configuration)
        }

        activeDags.values().forEach {
            allDags.addDags(it)
        }

        if (includeSavedDagsInReport && savedDags !is FileDagStore) {
            savedDags.values().forEach {
                allDags.addDags(it)
            }
        }

        reportActor.forward(allDags.build())
    }

    override fun handleNewDag(dag: YadelModel.Dag) {
        if (activeDags.size() > ActorUtilities.MaxActiveDags) {
            unprocessedDags.set(dag.id, dag.toBuilder())
        }
        else {
            activeDags.set(dag.id, dag.toBuilder())
        }
    }

    override fun updateSenderStatus(newState: YadelModel.WorkerState,
                                    setTaskToUnprocessed: Boolean,
                                    sender: IActor) {
        val workerKey = sender.key()
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

    override fun getOpenWorker(): ConfigurationActorRef? {
        val openWorkers = this.workers.values.filter { it.configuration.state == YadelModel.WorkerState.IDLE }
        if (openWorkers.isEmpty()) {
            return null
        }
        return openWorkers.first()
    }

    override fun freeAnyWorkersRunningOverAllowedAmount() {
        val now = LocalDateTime.now()

        val workersToReset = HashSet<String>()
        this.workers.keys.forEach {
            val worker = this.workers[it]!!

            if (worker.configuration.state == YadelModel.WorkerState.WORKING) {
                val taskStartTime = LocalDateTime.parse(worker.configuration.taskStartTime)

                val minutes = Minutes.minutesBetween(taskStartTime, now).minutes
                if (minutes > minutesBeforeTaskReset) {
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

    override fun handleRegistration(sender: IActor) {
        this.log.info("handling registration")
        val key = sender.key()

        if (workers.containsKey(key)) {
            // going to reset the task status, if there was one
            updateSenderStatus(YadelModel.WorkerState.IDLE, true, sender)
        }

        // new configuration and reference
        val newConfiguration = YadelModel.WorkerConfiguration.newBuilder()
                .setHost(sender.host())
                .setPort(sender.port())
                .setIp(key)
                .setInitializedTime(LocalDateTime.now().toString())
                .setMinutesBeforeTaskReset(minutesBeforeTaskReset)
                .setState(YadelModel.WorkerState.IDLE)
        val tuple = ConfigurationActorRef(sender, newConfiguration.build())
        this.workers.put(key, tuple)
    }

    override fun handleDeleteDag(request: YadelReport.UIYadelRequest) {
        this.log.info("attempting to remove ${request.dagId}")
        if (activeDags.containsKey(request.dagId)) {
            activeDags.remove(request.dagId)
        }
        else if (savedDags.containsKey(request.dagId)) {
            savedDags.remove(request.dagId)
        }
    }

    override fun handleGetDagStatus(request: YadelReport.UIYadelRequest, sender: IActor) {
        if (activeDags.containsKey(request.dagId)) {
            val uiDag = ActorUtilities.buildUIDagFromDag(activeDags.get(request.dagId)!!)
            sender.tell(uiDag.build())
        }
        else if (savedDags.containsKey(request.dagId)) {
            val uiDag = ActorUtilities.buildUIDagFromDag(savedDags.get(request.dagId)!!)
            sender.tell(uiDag.build())
        }
    }

    override fun handleTermination(sender: IActor) {
        this.log.info("handling termination")
        if (this.workers.containsKey(sender.key())) {
            this.workers.remove(sender.key())
        }
    }

    override fun handleEnsureWorkersWorking() {
        log.info("Have ${this.activeDags.values().size} active dags with ${this.workers.size} workers:")
        log.info("with ${unprocessedDags.size()} unprocessed dags")
        if (true) {
            log.info("with ${savedDags.size()} saved dags")
        }

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
            this.log.info("${it.actorRef.key()}: ${it.configuration.state.name}")
        }
    }

    override fun handleTellWorkersToDoNewDagWork() {
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
                    val workerKey = openWorker.actorRef.key()
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

                    openWorker.actorRef.tell(task.build())
                }
            }
        }
    }
}