package org.roylance.yadel.api.actors

import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.api.services.file.FileDagStore
import org.roylance.yadel.api.services.memory.MemoryDagStore
import org.roylance.yadel.api.utilities.ActorUtilities

class ReportActor : UntypedActor() {
    val savedDags = FileDagStore(ActorUtilities.CommonDagFile)
    protected val log: LoggingAdapter = Logging.getLogger(this.context().system(), this)

    override fun onReceive(message: Any?) {
        log.info("received message")
        if (message is YadelModel.AllDags) {
            log.info("building report to send off")
            val dagReport = YadelReport.UIDagReport.newBuilder()
            message.workersList.forEach {
                dagReport.addWorkers(ActorUtilities.buildWorkerConfiguration(it))
            }

            val memoryDag = MemoryDagStore()
            message.dagsList.forEach { dag ->
                memoryDag.set(dag.id, dag.toBuilder())
            }

            val uiDags = ActorUtilities.buildDagTree(listOf(memoryDag, savedDags))
            dagReport.addAllDags(uiDags)

            sender.tell(dagReport.build(), self)
            log.info("sent off report to original sender")
        }
    }
}