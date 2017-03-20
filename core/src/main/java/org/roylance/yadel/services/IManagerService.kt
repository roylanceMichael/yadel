package org.roylance.yadel.services

import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.models.ConfigurationActorRef

interface IManagerService {
    fun getWorkers(): Map<String, ConfigurationActorRef>

    fun getIncludeSavedDagsInReport(): Boolean

    fun getSavedDags(): IDagStore
    fun activeDags(): IDagStore
    fun getUnprocessedDags(): IDagStore

    fun handleNewDag(dag: YadelModel.Dag)
    fun handleAddTaskToDag(addTaskToDag: YadelModel.AddTaskToDag)
    fun handleCompleteTask(completeTask: YadelModel.CompleteTask, sender: IActor)
    fun handleReport(includeUnprocessed: Boolean = true)
    fun handleDeleteDag(request: YadelReport.UIYadelRequest)
    fun handleGetDagStatus(request: YadelReport.UIYadelRequest, sender: IActor)
    fun updateSenderStatus(newState: YadelModel.WorkerState,
                           setTaskToUnprocessed: Boolean = false,
                           sender: IActor)
    fun getOpenWorker(task: YadelModel.Task.Builder): ConfigurationActorRef?
    fun freeAnyWorkersRunningOverAllowedAmount()
    fun handleRegistration(sender: IActor)
    fun handleTermination(sender: IActor)
    fun handleEnsureWorkersWorking()
    fun handleTellWorkersToDoNewDagWork()
    fun updateWorkerProperties(actor: IActor, properties: YadelModel.WorkerProperties)
}