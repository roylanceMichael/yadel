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
//                it.mutableUncompletedTasks.forEach { uncompletedTask ->
//                    this.log.info("${uncompletedTask.key}")
//                    this.log.info("dependencies: ${uncompletedTask.value.taskDefinition.dependencies.size} - ${uncompletedTask.value.taskDefinition.dependencies.values.map { it.id }.joinToString()}")
//                }
                this.log.info("${it.mutableProcessingTasks.size} processing tasks")
//                it.mutableProcessingTasks.forEach { processingTask ->
//                    this.log.info("${processingTask.key}")
//                    this.log.info("dependencies: ${processingTask.value.taskDefinition.dependencies.size} - ${processingTask.value.taskDefinition.dependencies.values.map { it.id }.joinToString()}")
//                }
                this.log.info("${it.mutableErroredTasks.size} error tasks")
//                it.mutableErroredTasks.forEach { errorTask ->
//                    this.log.info("${errorTask.key}")
//                    this.log.info("dependencies: ${errorTask.value.taskDefinition.dependencies.size} - ${errorTask.value.taskDefinition.dependencies.values.map { it.id }.joinToString()}")
//                }
                this.log.info("${it.mutableCompletedTasks.size} completed tasks")
//                it.mutableCompletedTasks.forEach { completedTask ->
//                    this.log.info("${completedTask.key}")
//                }

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
        if (p0 is YadelModels.Dag && !this.activeDags.containsKey(p0.dagDefinition.id)) {
            // are we processing this dag already?
            this.activeDags[p0.dagDefinition.id] = p0.toBuilder()

        }
        else if (p0 is YadelModels.AddTaskToDag && this.activeDags.containsKey(p0.parentTask.dagDefinition.id)) {
            val foundDag = this.activeDags[p0.parentTask.dagDefinition.id]!!

            if (foundDag.dagDefinitionBuilder.mutableFlattenedTasks.containsKey(p0.parentTask.id)) {
                val parentTask = foundDag.dagDefinitionBuilder.mutableFlattenedTasks[p0.parentTask.id]!!
                parentTask.dependencies[p0.newTask.id] = p0.newTask
                foundDag.dagDefinitionBuilder.mutableFlattenedTasks[p0.newTask.id] = p0.newTask

                val newTask = YadelModels.Task.newBuilder()
                        .setFirstContext(p0.firstContext)
                        .setSecondContext(p0.secondContext)
                        .setThirdContext(p0.thirdContext)
                        .build()

                foundDag.mutableUncompletedTasks[newTask.taskDefinition.id] = newTask
            }
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
