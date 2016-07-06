package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.ManagerBase
import org.roylance.yadel.api.models.YadelModels
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class SampleManager:ManagerBase() {
    override fun preStart() {
        super.preStart()
        val newDag = DagBuilder().build()
        this.self.tell(newDag, this.self)
    }

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)
    }
}
