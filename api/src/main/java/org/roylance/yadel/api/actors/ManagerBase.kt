package org.roylance.yadel.api.actors

import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import org.roylance.yadel.api.models.ConfigurationActorRef
import org.roylance.yadel.api.models.YadelModels
import org.roylance.yadel.api.models.YadelReports
import scala.concurrent.duration.Duration
import java.util.*
import java.util.concurrent.TimeUnit

open class ManagerBase :UntypedActor() {
    protected val workers = HashMap<String, ConfigurationActorRef>()
    protected val activeDags = HashMap<String, YadelModels.Dag.Builder>()

    // todo: keep completed dags around
    protected val log = Logging.getLogger(this.context().system(), this)

    override fun preStart() {
        val runnable = Runnable {
            this.log.info("Have ${this.activeDags.size} active dags with ${this.workers.size} workers:")
            this.activeDags.values.forEach {
                val availableTaskIds = getAllAvailableTaskIds(it)
                this.log.info("${it.dagDefinition.display} (${it.dagDefinition.id})")
                this.log.info("${it.mutableUncompletedTasks.size} uncompleted tasks")
                this.log.info("${it.mutableProcessingTasks.size} processing tasks")
                this.log.info("${it.mutableErroredTasks.size} error tasks")
                this.log.info("${it.mutableCompletedTasks.size} completed tasks")

                this.log.info("${availableTaskIds.size} task(s) can be executed right now with ${this.workers.values.count { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }} idle worker(s)")
            }
            this.workers.values.forEach {
                this.log.info("${it.actorRef.path().toString()}: ${it.configuration.state.name}")
            }
            this.self.tell(YadelModels.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING, this.self)
        }
        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
        // is this a dag from someone?
        if (p0 is YadelModels.Dag &&
                !this.activeDags.containsKey(p0.dagDefinition.id)) {
            // are we processing this dag already?
            this.activeDags[p0.dagDefinition.id] = p0.toBuilder()
        }
        else if (p0 is YadelModels.AddTaskToDag &&
                p0.hasNewTask() &&
                p0.newTask.hasDagDefinition() &&
                this.activeDags.containsKey(p0.newTask.dagDefinition.id)) {
            val foundDag = this.activeDags[p0.newTask.dagDefinition.id]!!
            foundDag.dagDefinitionBuilder.mutableFlattenedTasks[p0.newTask.id] = p0.newTask

            val newTask = YadelModels.Task.newBuilder()
                    .setFirstContext(p0.firstContext)
                    .setSecondContext(p0.secondContext)
                    .setThirdContext(p0.thirdContext)
                    .setTaskDefinition(p0.newTask)
                    .setStartDate(Date().time)
                    .setExecutionDate(Date().time)

            foundDag.mutableUncompletedTasks[p0.newTask.id] = newTask.build()
        }
        else if (p0 is YadelModels.CompleteTask) {
            this.updateSenderStatus(YadelModels.WorkerState.IDLE)

            // handle complete task
            if (this.activeDags.containsKey(p0.taskDefinition.dagDefinition.id) &&
                this.activeDags[p0.taskDefinition.dagDefinition.id]!!.mutableProcessingTasks.containsKey(p0.taskDefinition.id)) {

                this.activeDags[p0.taskDefinition.dagDefinition.id]!!.mutableProcessingTasks.remove(p0.taskDefinition.id)
                if (p0.isError) {
                    this.activeDags[p0.taskDefinition.dagDefinition.id]!!.mutableErroredTasks[p0.taskDefinition.id] = p0
                }
                else {
                    this.activeDags[p0.taskDefinition.dagDefinition.id]!!.mutableCompletedTasks[p0.taskDefinition.id] = p0
                }
            }
        }
        else if (p0 is YadelModels.WorkerToManagerMessageType &&
                YadelModels.WorkerToManagerMessageType.REGISTRATION.equals(p0)) {
            this.log.info("handling registration")
            this.handleRegistration()
        }
        else if (p0 is Terminated) {
            this.log.info("handling termination")
            this.handleTermination(p0)
        }
        else if (p0 is YadelReports.UIRequest) {
            if (p0.requestType.equals(YadelReports.UIRequests.REPORT_DAGS)) {
                this.handleReport()
            }
            if (p0.requestType.equals(YadelReports.UIRequests.DELETE_DAG)) {
                if (this.activeDags.containsKey(p0.dagId)) {
                    this.activeDags.remove(p0.dagId)
                }
            }
        }
        else if (p0 is YadelReports.UIRequests) {
            if (YadelReports.UIRequests.REPORT_DAGS.equals(p0)) {
                this.handleReport()
            }
        }

        this.tellWorkersToDoNewDagWork()
    }

    protected fun tellWorkersToDoNewDagWork() {
        this.activeDags.values.forEach { foundActiveDag ->
            val taskIdsToProcess = getAllAvailableTaskIds(foundActiveDag)

            taskIdsToProcess.forEach { uncompletedTaskId ->
                val openWorker = this.getOpenWorker()
                if (openWorker != null) {
                    val workerKey = this.getActorRefKey(openWorker.actorRef)
                    val foundTuple = this.workers[workerKey]
                    val newBuilder = foundTuple!!.configuration.toBuilder()
                    newBuilder.state = YadelModels.WorkerState.WORKING
                    this.workers[workerKey] = ConfigurationActorRef(openWorker.actorRef, newBuilder.build())

                    val task = foundActiveDag.mutableUncompletedTasks[uncompletedTaskId]!!

                    foundActiveDag.mutableUncompletedTasks.remove(uncompletedTaskId)
                    val updatedTask = task.toBuilder().setExecutionDate(Date().time).setStartDate(Date().time)
                    foundActiveDag.mutableProcessingTasks[uncompletedTaskId] = updatedTask.build()

                    openWorker.actorRef.tell(task, this.self)
                }
            }
        }
    }

    private fun handleReport() {
        val dagReport = YadelReports.UIDagReport.newBuilder()
        this.workers.values.forEach {
            val workerConfiguration = YadelReports.UIWorkerConfiguration
                    .newBuilder()
                    .setHost(it.configuration.host)
                    .setInitializedTime(it.configuration.initializedTime)
                    .setIp(it.configuration.ip)
                    .setPort(it.configuration.port)
                    .setState(if (YadelModels.WorkerState.WORKING.equals(it.configuration.state)) YadelReports.UIWorkerState.CURRENTLY_WORKING else YadelReports.UIWorkerState.CURRENTLY_IDLE)

            dagReport.addWorkers(workerConfiguration)
        }

        this.activeDags.values.forEach { dag ->
            val newDag = YadelReports.UIDag.newBuilder()
                    .setId(dag.dagDefinition.id)
                    .setDisplay(dag.dagDefinition.display)
                    .setIsProcessing(dag.processingTasks.size > 0)
                    .setIsError(dag.erroredTasks.size > 0)
                    .setIsCompleted(dag.uncompletedTasks.size == 0)
                    .setNumberCompleted(dag.completedTasks.size)
                    .setNumberErrored(dag.erroredTasks.size)
                    .setNumberProcessing(dag.processingTasks.size)
                    .setNumberUnprocessed(dag.uncompletedTasks.size)

            dag.dagDefinition
                    .flattenedTasks
                    .values
                    .forEach { task ->
                        val newNode = YadelReports.UINode.newBuilder()
                                .setId(task.id)
                                .setDisplay(task.display)
                                .setIsError(dag.erroredTasks.containsKey(task.id))
                                .setIsProcessing(dag.processingTasks.containsKey(task.id))
                                .setIsCompleted(dag.completedTasks.containsKey(task.id))

                        newDag.mutableNodes[newNode.id] = newNode.build()
                        task.dependencies.keys.forEach { dependency ->
                            val newEdge = YadelReports.UIEdge.newBuilder()
                                    .setNodeId1(task.id)
                                    .setNodeId2(dependency)

                            newDag.addEdges(newEdge)
                        }
                    }

            dagReport.addDags(newDag.build())
        }

        // respond with message, but encode in base 64
        this.sender.tell(dagReport.build(), this.self)
    }

    private fun getAllAvailableTaskIds(foundActiveDag:YadelModels.Dag.Builder):List<String> {
        val taskIdsToProcess = ArrayList<String>()
        foundActiveDag.mutableUncompletedTasks.values.forEach { uncompletedTask ->
            if (uncompletedTask.taskDefinition.dependencies.size == 0) {
                // process this
                taskIdsToProcess.add(uncompletedTask.taskDefinition.id)
            } else if (uncompletedTask.taskDefinition.dependencies.values.all { dependency -> foundActiveDag.mutableCompletedTasks.containsKey(dependency.id) }) {
                // process this
                taskIdsToProcess.add(uncompletedTask.taskDefinition.id)
            }
        }

        return taskIdsToProcess
    }

    private fun updateSenderStatus(newState:YadelModels.WorkerState) {
        val workerKey = this.getActorRefKey(this.sender)
        if (this.workers.containsKey(workerKey)) {
            val foundTuple = this.workers[workerKey]
            val newBuilder = foundTuple!!.configuration.toBuilder()
            newBuilder.state = newState
            this.workers[workerKey] = ConfigurationActorRef(this.sender, newBuilder.build())
        }
    }

    protected fun getOpenWorker():ConfigurationActorRef? {
        val openWorkers = this.workers.values.filter { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }
        if (openWorkers.isEmpty()) {
            return null
        }
        return openWorkers.first()
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
