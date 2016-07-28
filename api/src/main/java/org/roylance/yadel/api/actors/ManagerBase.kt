package org.roylance.yadel.api.actors

import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.api.models.ConfigurationActorRef
import scala.concurrent.duration.Duration
import java.lang.management.ManagementFactory
import java.util.*
import java.util.concurrent.TimeUnit

open class ManagerBase :UntypedActor() {
    protected val workers = HashMap<String, ConfigurationActorRef>()
    protected val activeDags = HashMap<String, YadelModel.Dag.Builder>()

    protected val log: LoggingAdapter = Logging.getLogger(this.context().system(), this)

    override fun preStart() {
        System.out.println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        val runnable = Runnable {
            this.self.tell(YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING, this.self)
        }
        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
        // is this a dag from someone?
        if (p0 is YadelModel.Dag) {
            this.activeDags[p0.id] = p0.toBuilder()
        }
        else if (p0 is YadelModel.AddTaskToDag && p0.hasNewTask() && this.activeDags.containsKey(p0.newTask.dagId)) {
            this.log.info("looking for ${p0.newTask.dagId}")
            val foundDag = this.activeDags[p0.newTask.dagId]!!
            foundDag.addUncompletedTasks(p0.newTask).addFlattenedTasks(p0.newTask)
            this.log.info("saved task to dag")
        }
        else if (p0 is YadelModel.CompleteTask && this.activeDags.containsKey(p0.task.dagId)) {
            this.updateSenderStatus(YadelModel.WorkerState.IDLE)

            val foundDagBuilder = this.activeDags[p0.task.dagId]!!
            val existingTask = foundDagBuilder.processingTasksBuilderList.firstOrNull { it.id.equals(p0.task.id) }
            if (existingTask != null) {
                val indexToRemove = foundDagBuilder.processingTasksBuilderList.indexOf(existingTask)
                foundDagBuilder.removeProcessingTasks(indexToRemove)
            }
            else {
                this.log.info("could not find ${p0.task.id} in the processing tasks, for some reason...")
            }

            if (p0.isError) {
                foundDagBuilder.addErroredTasks(p0.task)
            }
            else {
                foundDagBuilder.addCompletedTasks(p0.task)
            }
        }
        else if (p0 is YadelModel.WorkerToManagerMessageType &&
                YadelModel.WorkerToManagerMessageType.REGISTRATION.equals(p0)) {
            this.log.info("handling registration")
            this.handleRegistration()
        }
        else if (p0 is Terminated) {
            this.log.info("handling termination")
            this.handleTermination(p0)
        }
        else if (p0 is YadelReport.UIRequest) {
            if (p0.requestType.equals(YadelReport.UIRequests.REPORT_DAGS)) {
                this.handleReport()
            }
            if (p0.requestType.equals(YadelReport.UIRequests.DELETE_DAG) &&
                this.activeDags.containsKey(p0.dagId)) {
                this.log.info("attempting to remove ${p0.dagId}")
                this.activeDags.remove(p0.dagId)
            }
        }
        else if (p0 is YadelReport.UIRequests) {
            if (YadelReport.UIRequests.REPORT_DAGS.equals(p0)) {
                this.handleReport()
            }
        }
        else if (p0 is YadelModel.ManagerToManagerMessageType &&
                YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING.equals(p0)) {
            this.log.info("Have ${this.activeDags.size} active dags with ${this.workers.size} workers:")

            this.activeDags.values.forEach { actualDag ->
                val availableTaskIds = getAllAvailableTaskIds(actualDag)
                this.log.info("${actualDag.display} (${actualDag.id})")
                this.log.info("${actualDag.uncompletedTasksCount} uncompleted tasks")
                this.log.info("${actualDag.processingTasksCount} processing tasks")
                this.log.info("${actualDag.erroredTasksCount} error tasks")
                this.log.info("${actualDag.completedTasksCount} completed tasks")

                this.log.info("${availableTaskIds.size} task(s) can be executed right now with ${this.workers.values.count { it.configuration.state.equals(YadelModel.WorkerState.IDLE) }} idle worker(s)")
            }
            this.workers.values.forEach {
                this.log.info("${it.actorRef.path().toString()}: ${it.configuration.state.name}")
            }
        }

        this.tellWorkersToDoNewDagWork()
    }

    protected fun tellWorkersToDoNewDagWork() {
        if (this.workers.values.filter { it.configuration.state.equals(YadelModel.WorkerState.IDLE) }.size == 0) {
            return
        }

        this.activeDags.values.forEach { foundActiveDag ->
            val taskIdsToProcess = getAllAvailableTaskIds(foundActiveDag)

            taskIdsToProcess.forEach { uncompletedTaskId ->
                val openWorker = getOpenWorker()
                if (openWorker != null) {
                    val workerKey = getActorRefKey(openWorker.actorRef)
                    val foundTuple = workers[workerKey]
                    val newBuilder = foundTuple!!.configuration.toBuilder()
                    newBuilder.state = YadelModel.WorkerState.WORKING
                    workers[workerKey] = ConfigurationActorRef(openWorker.actorRef, newBuilder.build())

                    val task = foundActiveDag.uncompletedTasksBuilderList.first { it.id.equals(uncompletedTaskId) }
                    val taskIdx = foundActiveDag.uncompletedTasksBuilderList.indexOf(task!!)
                    foundActiveDag.removeUncompletedTasks(taskIdx)

                    val updatedTask = task.setExecutionDate(Date().time).setStartDate(Date().time)
                    foundActiveDag.addProcessingTasks(updatedTask.build())

                    openWorker.actorRef.tell(task.build(), self)
                }
            }
        }
    }

    private fun handleReport() {
        val dagReport = YadelReport.UIDagReport.newBuilder()
        this.workers.values.forEach {
            val workerConfiguration = YadelReport.UIWorkerConfiguration
                    .newBuilder()
                    .setHost(it.configuration.host)
                    .setInitializedTime(it.configuration.initializedTime)
                    .setIp(it.configuration.ip)
                    .setPort(it.configuration.port)
                    .setState(if (YadelModel.WorkerState.WORKING.equals(it.configuration.state)) YadelReport.UIWorkerState.CURRENTLY_WORKING else YadelReport.UIWorkerState.CURRENTLY_IDLE)

            dagReport.addWorkers(workerConfiguration)
        }

        this.activeDags.values.forEach { dag ->
            val newDag = YadelReport.UIDag.newBuilder()
                    .setId(dag.id)
                    .setDisplay(dag.display)
                    .setIsProcessing(dag.processingTasksCount > 0)
                    .setIsError(dag.erroredTasksCount > 0)
                    .setIsCompleted(dag.uncompletedTasksCount == 0)
                    .setNumberCompleted(dag.completedTasksCount)
                    .setNumberErrored(dag.erroredTasksCount)
                    .setNumberProcessing(dag.processingTasksCount)
                    .setNumberUnprocessed(dag.uncompletedTasksCount)

            dag
                    .flattenedTasksList
                    .forEach { task ->
                        val newNode = YadelReport.UINode.newBuilder()
                                .setId(task.id)
                                .setDisplay(task.display)
                                .setIsError(dag.erroredTasksList.any { it.id.equals(task.id) })
                                .setIsProcessing(dag.processingTasksList.any { it.id.equals(task.id) })
                                .setIsCompleted(dag.completedTasksList.any { it.id.equals(task.id) })

                        newDag.addNodes(newNode)
                        task.dependenciesList.forEach { dependency ->
                            val newEdge = YadelReport.UIEdge.newBuilder()
                                    .setNodeId1(task.id)
                                    .setNodeId2(dependency.parentTaskId)

                            newDag.addEdges(newEdge)
                        }
                    }

            dagReport.addDags(newDag.build())
        }

        // respond with message, but encode in base 64
        this.sender.tell(dagReport.build(), this.self)
    }

    private fun getAllAvailableTaskIds(foundActiveDag: YadelModel.Dag.Builder):List<String> {
        val taskIdsToProcess = ArrayList<String>()
        foundActiveDag.uncompletedTasksList.forEach { uncompletedTask ->
            if (uncompletedTask.dependenciesCount == 0) {
                // process this
                taskIdsToProcess.add(uncompletedTask.id)
            } else if (uncompletedTask.dependenciesList.all { dependency ->
                foundActiveDag.completedTasksList.any { it.id.equals(dependency.parentTaskId) } }) {
                // process this
                taskIdsToProcess.add(uncompletedTask.id)
            }
        }

        return taskIdsToProcess
    }

    private fun updateSenderStatus(newState:YadelModel.WorkerState) {
        val workerKey = this.getActorRefKey(this.sender)
        if (this.workers.containsKey(workerKey)) {
            val foundTuple = this.workers[workerKey]
            val newBuilder = foundTuple!!.configuration.toBuilder()
            newBuilder.state = newState
            this.workers[workerKey] = ConfigurationActorRef(this.sender, newBuilder.build())
        }
    }

    protected fun getOpenWorker():ConfigurationActorRef? {
        val openWorkers = this.workers.values.filter { it.configuration.state.equals(YadelModel.WorkerState.IDLE) }
        if (openWorkers.isEmpty()) {
            return null
        }
        return openWorkers.first()
    }

    private fun handleRegistration() {
        this.context.watch(this.sender)
        val key = this.getActorRefKey(this.sender)

        val newConfiguration = YadelModel.WorkerConfiguration.newBuilder()
                .setHost(this.sender.path().address().host().get())
                .setPort(this.sender.path().address().port().get().toString())
                .setIp(key)
                .setInitializedTime(Date().time)
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
        private val OneMinute = Duration.create(1, TimeUnit.MINUTES)
    }
}
