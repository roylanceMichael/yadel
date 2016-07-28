package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.ManagerBase
import org.roylance.yadel.YadelModel
import scala.concurrent.duration.Duration
import java.lang.management.ManagementFactory
import java.util.concurrent.TimeUnit

class SampleManager:ManagerBase() {
    override fun preStart() {
        super.preStart()
        System.out.println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        val newDag = DagBuilder().build()
        this.self.tell(newDag, this.self)
    }

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)
    }
}
