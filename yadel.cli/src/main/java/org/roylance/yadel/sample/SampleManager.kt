package org.roylance.yadel.sample

import org.roylance.yadel.actors.ManagerBase
import java.lang.management.ManagementFactory

class SampleManager : ManagerBase() {
    override fun preStart() {
        super.preStart()
        println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
        var i = 0
        while (i < 50) {
            val newDag = DagBuilder().build()
            self.tell(newDag, this.self)
            i++
        }
    }

    override fun onReceive(message: Any?) {
        super.onReceive(message)
    }
}
