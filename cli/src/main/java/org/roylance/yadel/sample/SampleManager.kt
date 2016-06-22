package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.ManagerBase
import org.roylance.yadel.api.models.YadelModels
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class SampleManager:ManagerBase() {
    override fun preStart() {
        super.preStart()

        val runnable = Runnable {
            if (this.activeDags.size == 0) {
                val newDag = DagBuilder().build()
                this.self.tell(newDag, this.self)
            }
        }

        this.context.system().scheduler().schedule(OneMinute, OneMinute, runnable, this.context.system().dispatcher())
    }

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)
    }

    companion object {
        private val OneMinute = Duration.create(1, TimeUnit.SECONDS);
    }
}
