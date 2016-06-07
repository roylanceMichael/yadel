package org.roylance.yadel.api.services

import org.roylance.yadel.api.actors.ManagerBase
import org.roylance.yadel.api.actors.WorkerBase
import org.roylance.yadel.api.enums.CommonTokens

object MainLogic {
    fun <T:ManagerBase, K:WorkerBase> main(args:Array<String>, managerClass:Class<T>, workerClass:Class<K>) {
        println("expecting: hostname port seedNode1Host seedNode1Port seedNode2Host seedNode2Port " +
                "<port ${CommonTokens.TwoThreeFourFour} will always start the manager, other ports will start workers>")

        val hostName = if (isEmptyOrLessThanN(args, 1)) CommonTokens.LocalHost else args[0]
        val port = if (isEmptyOrLessThanN(args, 2)) CommonTokens.Zero else args[1]
        if (CommonTokens.TwoThreeFourFour == port) {
            ManagerBuilder(hostName, port, managerClass).build()
        } else {
            WorkerBuilder(hostName, port, workerClass).build()
        }
    }

    private fun isEmptyOrLessThanN(args: Array<String>?, n: Int): Boolean {
        return args == null || args.size < n
    }
}
