package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.ManagerBase
import org.roylance.yadel.api.models.YadelModels
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class SampleManager:ManagerBase() {
    override fun preStart() {
        super.preStart()

        val runnable = Runnable {
            if (this.workers.size < 5 && this.workers.size > 0) {
                this.log.info("spawning a new worker")
                val spawnModel = YadelModels.SpawnMessage.newBuilder()
                        .setSeedHost1("127.0.0.1")
                        .setSeedPort1("2345")
                        .setSeedHost2("127.0.0.1")
                        .setSeedPort2("2346")

                val spawnWorkerMessage = YadelModels.ManagerToWorkerMessage.newBuilder()
                    .setType(YadelModels.ManagerToWorkerMessageType.SPAWN_NEW_WORKER_AT_YOUR_LOCATION)
                    .setSpawnMessage(spawnModel)

                val firstWorker = this.workers.values.first()
                firstWorker.actorRef.tell(spawnWorkerMessage.build(), this.self)
            }

            this.log.info("telling a worker to do new work")
            val newMessage = YadelModels.ServiceToManagerMessage.newBuilder()
            newMessage.name = "simple count"
            newMessage.type = YadelModels.ServiceToManagerType.DO_NEW_WORK
            this.tellWorkerToDoNewWork(newMessage.build())
            this.tellWorkerToDoNewWork(newMessage.build())
            this.tellWorkerToDoNewWork(newMessage.build())
            this.tellWorkerToDoNewWork(newMessage.build())
            this.tellWorkerToDoNewWork(newMessage.build())

            this.log.info("looking at workers now...")
            this.workers.forEach { s, configurationActorRef ->
                this.log.info("${configurationActorRef.actorRef.path().address().toString()} ${configurationActorRef.configuration.state.name}")
            }
        }

        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable,this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)
    }

    companion object {
        private val OneMinute = Duration.create(1, TimeUnit.MINUTES);
    }
}
