package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.ManagerBase
import java.lang.management.ManagementFactory

class SampleManager:ManagerBase() {
    override fun preStart() {
        super.preStart()
        println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        val newDag = DagBuilder().build()
        this.self.tell(newDag, this.self)
    }

    override fun onReceive(message: Any?) {
        super.onReceive(message)
    }
}
