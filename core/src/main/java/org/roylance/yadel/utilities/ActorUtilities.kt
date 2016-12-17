package org.roylance.yadel.utilities

import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.services.IDagStore
import scala.concurrent.duration.Duration
import java.util.*
import java.util.concurrent.TimeUnit

object ActorUtilities {
    const val MB = 1024*1024
    const val MaxActiveDags = 5
    const val CommonDagFile = "savedDags.bin"
    const val UnprocessedDags = "unprocessedDags.bin"

    val OneMinute = Duration.create(1, TimeUnit.MINUTES)!!
    val RunTime = Runtime.getRuntime()!!

    fun buildMemoryLogMessage(): String {
        return "used memory: ${(ActorUtilities.RunTime.totalMemory() - ActorUtilities.RunTime.freeMemory())/ActorUtilities.MB}, total memory: ${ActorUtilities.RunTime.totalMemory()/ ActorUtilities.MB}"
    }

    fun isDagComplete(dag: YadelModel.Dag.Builder): Boolean {
        return dag.completedTasksCount == dag.flattenedTasksCount
    }

    fun isDagError(dag: YadelModel.Dag.Builder): Boolean {
        return dag.erroredTasksCount > 0
    }

    fun canProcessDag(dag: YadelModel.Dag.Builder, activeDags: IDagStore): Boolean {
        if (dag.hasParent() && activeDags.containsKey(dag.parent.id)) {
            val parent = activeDags.get(dag.parent.id)!!
            return isDagComplete(parent) && !isDagError(dag)
        }

        return !isDagError(dag)
    }

    fun getAllAvailableTaskIds(foundActiveDag: YadelModel.Dag.Builder): List<String> {
        val taskIdsToProcess = ArrayList<String>()
        foundActiveDag.uncompletedTasksList.forEach { uncompletedTask ->
            if (uncompletedTask.dependenciesCount == 0) {
                // process this
                taskIdsToProcess.add(uncompletedTask.id)
            } else if (uncompletedTask.dependenciesList.all { dependency ->
                foundActiveDag.completedTasksList.any { it.id == dependency.parentTaskId } }) {
                // process this
                taskIdsToProcess.add(uncompletedTask.id)
            }
        }

        return taskIdsToProcess
    }

    fun buildUIDagFromDag(dag: YadelModel.Dag.Builder): YadelReport.UIDag.Builder {
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
                .completedTasksList
                .forEach { task ->
                    newDag.addNodes(buildUINodeFromTask(task, false, false, true))
                    task.dependenciesList.forEach { dependency ->
                        val newEdge = YadelReport.UIEdge.newBuilder()
                                .setNodeId1(task.id)
                                .setNodeId2(dependency.parentTaskId)

                        newDag.addEdges(newEdge)
                    }
                }

        dag
                .uncompletedTasksList
                .forEach { task ->
                    newDag.addNodes(buildUINodeFromTask(task, false, false, false))
                    task.dependenciesList.forEach { dependency ->
                        val newEdge = YadelReport.UIEdge.newBuilder()
                                .setNodeId1(task.id)
                                .setNodeId2(dependency.parentTaskId)

                        newDag.addEdges(newEdge)
                    }
                }

        dag
                .erroredTasksList
                .forEach { task ->
                    newDag.addNodes(buildUINodeFromTask(task, true, false, false))
                    task.dependenciesList.forEach { dependency ->
                        val newEdge = YadelReport.UIEdge.newBuilder()
                                .setNodeId1(task.id)
                                .setNodeId2(dependency.parentTaskId)

                        newDag.addEdges(newEdge)
                    }
                }

        dag
                .processingTasksList
                .forEach { task ->
                    newDag.addNodes(buildUINodeFromTask(task, false, true, false))
                    task.dependenciesList.forEach { dependency ->
                        val newEdge = YadelReport.UIEdge.newBuilder()
                                .setNodeId1(task.id)
                                .setNodeId2(dependency.parentTaskId)

                        newDag.addEdges(newEdge)
                    }
                }

        return newDag
    }

    fun buildUINodeFromTask(task: YadelModel.Task, isError: Boolean, isProcessing: Boolean, isCompleted: Boolean): YadelReport.UINode {
        return YadelReport.UINode.newBuilder()
                .setId(task.id)
                .setDisplay(task.display)
                .setIsError(isError)
                .setIsProcessing(isProcessing)
                .setIsCompleted(isCompleted)
                .addAllLogs(task.logsList.map { YadelReport.UILog.newBuilder().setId(it.id).setMessage(it.message).build() })
                .build()
    }

    fun buildDagTree(dags: List<IDagStore>): List<YadelReport.UIDag> {
        val flattenedUIDags = HashMap<String, YadelReport.UIDag.Builder>()

        dags.forEach {
            it.values().forEach { dag ->
                val uiDag = buildUIDagFromDag(dag)
                flattenedUIDags[uiDag.id] = uiDag
            }
        }

        dags.forEach {
            it.values().forEach { dag ->
                val currentDag = flattenedUIDags[dag.id]!!
                if (dag.hasParent() && flattenedUIDags.containsKey(dag.parent.id)) {
                    val foundParent = flattenedUIDags[dag.parent.id]!!
                    foundParent.addChildren(currentDag)
                }
            }
        }

        val rootDags = ArrayList<YadelReport.UIDag>()
        dags.forEach {
            it.values().forEach { dag ->
                if (!dag.hasParent()) {
                    rootDags.add(flattenedUIDags[dag.id]!!.build())
                }
            }
        }

        return rootDags
    }

    fun buildWorkerConfiguration(configurationActorRef: YadelModel.WorkerConfiguration): YadelReport.UIWorkerConfiguration {
        val workerConfiguration = YadelReport.UIWorkerConfiguration
                .newBuilder()
                .setHost(configurationActorRef.host)
                .setInitializedTime(configurationActorRef.initializedTime)
                .setIp(configurationActorRef.ip)
                .setPort(configurationActorRef.port)
                .setState(if (YadelModel.WorkerState.WORKING == configurationActorRef.state) YadelReport.UIWorkerState.CURRENTLY_WORKING else YadelReport.UIWorkerState.CURRENTLY_IDLE)
                .setTaskStartTime(configurationActorRef.taskStartTime)

        if (configurationActorRef.hasDag()) {
            workerConfiguration.dagDisplay = configurationActorRef.dag.display
        }
        if (configurationActorRef.hasTask()) {
            workerConfiguration.taskDisplay = configurationActorRef.task.display
        }
        if (configurationActorRef.taskStartTime.isNotEmpty()) {
            workerConfiguration.taskWorkingTimeDisplay = "${Minutes.minutesBetween(LocalDateTime.parse(configurationActorRef.taskStartTime), LocalDateTime.now()).minutes} minutes"
        }

        return workerConfiguration.build()
    }
}