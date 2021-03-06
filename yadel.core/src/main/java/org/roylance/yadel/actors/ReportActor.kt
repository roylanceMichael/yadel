package org.roylance.yadel.actors

import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.roylance.yadel.YadelModel
import org.roylance.yadel.YadelReport
import org.roylance.yadel.services.IDagStore
import org.roylance.yadel.services.file.FileDagStore
import org.roylance.yadel.services.memory.MemoryDagStore
import org.roylance.yadel.utilities.ActorUtilities
import java.util.*

class ReportActor : UntypedActor() {
    private val savedDags = FileDagStore(ActorUtilities.CommonDagFile)
    private val unprocessedDags = FileDagStore(ActorUtilities.UnprocessedDags)

    private val log: LoggingAdapter = Logging.getLogger(this.context().system(), this)

    override fun onReceive(message: Any?) {
        log.info("received message")
        if (message is YadelModel.AllDags) {
            log.info("building report to send off")
            val dagReport = YadelReport.UIDagReport.newBuilder()
            dagReport.unprocessedDags = unprocessedDags.size()
            dagReport.savedDags = savedDags.size()
            dagReport.usedManagerMemory = "${((ActorUtilities.RunTime.totalMemory() - ActorUtilities.RunTime.freeMemory())/ ActorUtilities.MB)}"
            dagReport.totalManagerMemory = "${ActorUtilities.RunTime.totalMemory()/ ActorUtilities.MB}"

            message.workersList.forEach {
                dagReport.addWorkers(ActorUtilities.buildWorkerConfiguration(it))
            }

            val memoryDag = MemoryDagStore()
            message.dagsList.forEach { dag ->
                memoryDag.set(dag.id, dag.toBuilder())
            }

            val dagList = ArrayList<IDagStore>()
            dagList.add(memoryDag)
            if (message.includeUnprocessed) {
                dagList.add(unprocessedDags)
            }
            if (message.includeFileSaved) {
                dagList.add(savedDags)
            }

            val uiDags = ActorUtilities.buildDagTree(dagList)
            dagReport.addAllDags(uiDags)

            sender.tell(dagReport.build(), self)
            log.info("sent off report to original sender")
        }
    }
}