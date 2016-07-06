package org.roylance.yadel.api.actors

import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import com.google.protobuf.Message
import org.roylance.yadel.api.models.ConfigurationActorRef
import org.roylance.yadel.api.models.YadelModels
import org.roylance.yadel.api.models.YadelReports
import org.roylance.yadel.api.utilities.YadelModelsProtoGenerator
import org.roylance.yaorm.services.jdbc.JDBCGranularDatabaseProtoService
import org.roylance.yaorm.services.proto.EntityMessageService
import org.roylance.yaorm.services.proto.EntityProtoService
import org.roylance.yaorm.services.proto.IMessageStreamer
import org.roylance.yaorm.services.sqlite.SQLiteConnectionSourceFactory
import org.roylance.yaorm.services.sqlite.SQLiteGeneratorService
import scala.concurrent.duration.Duration
import java.util.*
import java.util.concurrent.TimeUnit

open class ManagerBase :UntypedActor() {
    protected val workers = HashMap<String, ConfigurationActorRef>()

    protected val log = Logging.getLogger(this.context().system(), this)
    protected val entityMessageService:EntityMessageService

    private val sourceConnection:SQLiteConnectionSourceFactory

    init {
        this.sourceConnection = SQLiteConnectionSourceFactory(DatabaseLocation)
        val connection = this.sourceConnection.connectionSource

        val granularDatabaseService = JDBCGranularDatabaseProtoService(
                this.sourceConnection.connectionSource,
                false)
        val sqliteGeneratorService = SQLiteGeneratorService()
        val entityService = EntityProtoService(null, granularDatabaseService, sqliteGeneratorService)
        this.entityMessageService = EntityMessageService(YadelModelsProtoGenerator(), entityService)
        this.entityMessageService.createEntireSchema(YadelModels.Dag.getDefaultInstance())
    }

    override fun preStart() {
        val runnable = Runnable {
            this.self.tell(YadelModels.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING, this.self)
        }
        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun postStop() {
        super.postStop()
        this.sourceConnection.close()
    }

    override fun onReceive(p0: Any?) {
        // is this a dag from someone?
        if (p0 is YadelModels.Dag) {
            // save it...
            this.entityMessageService.merge(p0)
        }
        else if (p0 is YadelModels.AddTaskToDag &&
                p0.hasNewTask()) {
            this.log.info("looking for ${p0.newTask.dagId}")
            val foundDag = this.entityMessageService.get(YadelModels.Dag.getDefaultInstance(), p0.newTask.dagId) ?: return

            val newDagBuilder = foundDag.toBuilder().addUncompletedTasks(p0.newTask).addFlattenedTasks(p0.newTask)
            this.entityMessageService.merge(newDagBuilder.build())
            this.log.info("saved task to dag")
        }
        else if (p0 is YadelModels.CompleteTask) {
            this.updateSenderStatus(YadelModels.WorkerState.IDLE)
            val foundDag = this.entityMessageService
                    .get(YadelModels.Dag.getDefaultInstance(), p0.task.dagId) ?: return

            val foundDagBuilder = foundDag.toBuilder()
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

            this.entityMessageService.merge(foundDagBuilder.build())
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
                this.log.info("attempting to remove ${p0.dagId}")

                val foundDag = this.entityMessageService.get(YadelModels.Dag.getDefaultInstance(), p0.dagId)
                if (foundDag != null) {
                    this.entityMessageService.delete(foundDag)
                    this.log.info("removed ${p0.dagId}")
                }
                else {
                    this.log.info("could not find ${p0.dagId} in the dag list. current dags are...")
                }
            }
        }
        else if (p0 is YadelReports.UIRequests) {
            if (YadelReports.UIRequests.REPORT_DAGS.equals(p0)) {
                this.handleReport()
            }
        }
        else if (p0 is YadelModels.ManagerToManagerMessageType &&
                YadelModels.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING.equals(p0)) {
            val dags = this.entityMessageService.getKeys(YadelModels.Dag.getDefaultInstance())

            this.log.info("Have ${dags.size} active dags with ${this.workers.size} workers:")

            dags.forEach {
                val actualDag = entityMessageService.get(YadelModels.Dag.getDefaultInstance(), it) ?: return@forEach
                val availableTaskIds = getAllAvailableTaskIds(actualDag.toBuilder())
                this.log.info("${actualDag.display} (${actualDag.id})")
                this.log.info("${actualDag.uncompletedTasksCount} uncompleted tasks")
                this.log.info("${actualDag.processingTasksCount} processing tasks")
                this.log.info("${actualDag.erroredTasksCount} error tasks")
                this.log.info("${actualDag.completedTasksCount} completed tasks")

                this.log.info("${availableTaskIds.size} task(s) can be executed right now with ${this.workers.values.count { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }} idle worker(s)")
            }
            this.workers.values.forEach {
                this.log.info("${it.actorRef.path().toString()}: ${it.configuration.state.name}")
            }
        }

        this.tellWorkersToDoNewDagWork()
    }

    protected fun tellWorkersToDoNewDagWork() {
        if (this.workers.values.filter { it.configuration.state.equals(YadelModels.WorkerState.IDLE) }.size == 0) {
            return
        }

        val streamer = object: IMessageStreamer {
            override fun <T : Message> stream(message: T) {
                val castedMessage = message as YadelModels.Dag
                val actualDag = entityMessageService.get(castedMessage, castedMessage.id)

                if (actualDag != null) {
                    val foundActiveDag = actualDag.toBuilder()
                    var anyChanges = false
                    val taskIdsToProcess = getAllAvailableTaskIds(foundActiveDag)

                    taskIdsToProcess.forEach { uncompletedTaskId ->
                        val openWorker = getOpenWorker()
                        if (openWorker != null) {
                            val workerKey = getActorRefKey(openWorker.actorRef)
                            val foundTuple = workers[workerKey]
                            val newBuilder = foundTuple!!.configuration.toBuilder()
                            newBuilder.state = YadelModels.WorkerState.WORKING
                            workers[workerKey] = ConfigurationActorRef(openWorker.actorRef, newBuilder.build())

                            val task = foundActiveDag.uncompletedTasksBuilderList.first { it.id.equals(uncompletedTaskId) }
                            val taskIdx = foundActiveDag.uncompletedTasksBuilderList.indexOf(task!!)
                            foundActiveDag.removeUncompletedTasks(taskIdx)

                            val updatedTask = task.setExecutionDate(Date().time).setStartDate(Date().time)
                            foundActiveDag.addProcessingTasks(updatedTask.build())

                            openWorker.actorRef.tell(task.build(), self)
                            anyChanges = true
                        }
                    }

                    if (anyChanges) {
                        entityMessageService.merge(foundActiveDag.build())
                    }
                }
            }
        }

        this.entityMessageService.getKeysStream(YadelModels.Dag.getDefaultInstance(), streamer)
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

        val streamer = object: IMessageStreamer {
            override fun <T : Message> stream(message: T) {
                val castedMessage = message as YadelModels.Dag
                val dag = entityMessageService.get(castedMessage, castedMessage.id) ?: return

                val newDag = YadelReports.UIDag.newBuilder()
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
                            val newNode = YadelReports.UINode.newBuilder()
                                    .setId(task.id)
                                    .setDisplay(task.display)
                                    .setIsError(dag.erroredTasksList.any { it.id.equals(task.id) })
                                    .setIsProcessing(dag.processingTasksList.any { it.id.equals(task.id) })
                                    .setIsCompleted(dag.completedTasksList.any { it.id.equals(task.id) })

                            newDag.addNodes(newNode)
                            task.dependenciesList.forEach { dependency ->
                                val newEdge = YadelReports.UIEdge.newBuilder()
                                        .setNodeId1(task.id)
                                        .setNodeId2(dependency.parentTaskId)

                                newDag.addEdges(newEdge)
                            }
                        }

                dagReport.addDags(newDag.build())
            }
        }

        this.entityMessageService.getKeysStream(YadelModels.Dag.getDefaultInstance(), streamer)

        // respond with message, but encode in base 64
        this.sender.tell(dagReport.build(), this.self)
    }

    private fun getAllAvailableTaskIds(foundActiveDag:YadelModels.Dag.Builder):List<String> {
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
        private val OneMinute = Duration.create(1, TimeUnit.MINUTES)
        const val DatabaseLocation = "manager.db"
    }
}
