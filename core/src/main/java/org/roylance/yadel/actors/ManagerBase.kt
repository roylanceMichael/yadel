package org.roylance.yadel.actors

import akka.actor.Props
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.models.ConfigurationActorRef
import org.roylance.yadel.services.ITaskLogger
import org.roylance.yadel.services.ManagerService
import org.roylance.yadel.services.file.FileDagStore
import org.roylance.yadel.services.memory.MemoryDagStore
import org.roylance.yadel.utilities.ActorUtilities
import java.util.*

abstract class ManagerBase: UntypedActor() {

    protected val reportActor = this.context().system().actorOf(Props.create(ReportActor::class.java))!!
    protected val log: LoggingAdapter
        get() = Logging.getLogger(this.context().system(), this)

    protected val logger = object: ITaskLogger {
        override val logs: List<YadelModel.Log>
            get() = ArrayList()

        override fun clearLogs() {
        }

        override fun debug(message: String) {
            log.info(message)
        }

        override fun error(message: String) {
            log.error(message)
        }

        override fun info(message: String) {
            log.info(message)
        }
    }

    protected val managerService = ManagerService(
            true,
            FileDagStore(ActorUtilities.CommonDagFile),
            MemoryDagStore(),
            FileDagStore(ActorUtilities.UnprocessedDags),
            HashMap<String, ConfigurationActorRef>(),
            ActorUtilities.buildIActor(self, context, reportActor),
            logger)

    override fun preStart() {
        super.preStart()

        println(ActorUtilities.buildMemoryLogMessage())

        super.preStart()
        val runnable = Runnable {
            this.self.tell(YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING, this.self)
        }
        this.context.system().scheduler().schedule(ActorUtilities.OneMinute,
                ActorUtilities.OneMinute,
                runnable,
                this.context.system().dispatcher())
    }

    override fun onReceive(message: Any?) {
        val senderActor = ActorUtilities.buildIActor(self, context, sender)
        // is this a dag from someone?
        if (message is YadelModel.WorkerState && message == YadelModel.WorkerState.IDLE) {
            managerService.updateSenderStatus(YadelModel.WorkerState.IDLE, true, senderActor)
        }
        else if (message is YadelModel.Dag) {
            managerService.handleNewDag(message)
        }
        // not going to try to add tasks to an unprocessed dag
        else if (message is YadelModel.AddTaskToDag &&
                message.hasNewTask()) {
            managerService.handleAddTaskToDag(message)
        }
        else if (message is YadelModel.CompleteTask) {
            managerService.handleCompleteTask(message, senderActor)
        }
        else if (message is YadelModel.WorkerToManagerMessageType &&
                YadelModel.WorkerToManagerMessageType.REGISTRATION == message) {
            managerService.handleRegistration(senderActor)
        }
        else if (message is Terminated) {
            managerService.handleTermination(ActorUtilities.buildIActor(self, context, message.actor))
        }
        else if (message is YadelReport.UIYadelRequest) {
            if (message.requestType == YadelReport.UIYadelRequestType.REPORT_DAGS) {
                managerService.handleReport()
            }
            if (message.requestType == YadelReport.UIYadelRequestType.DELETE_DAG) {
                managerService.handleDeleteDag(message)
            }
            if (message.requestType == YadelReport.UIYadelRequestType.GET_DAG_STATUS) {
                managerService.handleGetDagStatus(message, senderActor)
            }
        }
        else if (message is YadelReport.UIYadelRequestType &&
                YadelReport.UIYadelRequestType.REPORT_DAGS == message) {
            managerService.handleReport()
        }
        else if (message is YadelModel.ManagerToManagerMessageType &&
                YadelModel.ManagerToManagerMessageType.ENSURE_WORKERS_WORKING == message) {
            managerService.handleEnsureWorkersWorking()
        }

        managerService.handleTellWorkersToDoNewDagWork()
        managerService.freeAnyWorkersRunningOverAllowedAmount()
    }
}
