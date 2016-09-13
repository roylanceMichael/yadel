package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.WorkerBase
import org.roylance.yadel.YadelModel
import java.lang.management.ManagementFactory
import java.util.*

class SampleWorker:WorkerBase() {
    private val random = Random()

    override fun preStart() {
        super.preStart()
        Runtime.getRuntime().maxMemory()
        System.out.println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
    }

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)

        if (p0 is YadelModel.Task) {
            if (random.nextBoolean()) {
                Thread.sleep(600000)
                Thread.sleep(600000)
                Thread.sleep(100000)
            }

            val completeTask = this.handleMessage(p0.display)
            completeTask.task = p0
            this.completeTask(completeTask.build())

            if (random.nextBoolean()) {
                val randomInt = this.random.nextInt()
                val idAndDisplay = "$randomInt to ${randomInt + 100}"
                val newTask = YadelModel.Task.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDisplay(idAndDisplay)
                        .setDagId(p0.dagId)

                val dependency = YadelModel.TaskDependency.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setParentTaskId(p0.id)
                newTask.addDependencies(dependency)

                val addTask = YadelModel.AddTaskToDag
                        .newBuilder()
                        .setParentTask(p0)
                        .setNewTask(newTask)

                this.getManagerSelection()?.tell(addTask.build(), this.self)
                this.log.info("${addTask.toString()}")
            }
        }
    }

    private fun handleMessage(id:String):YadelModel.CompleteTask.Builder {
        val returnCompleteTask = YadelModel.CompleteTask.newBuilder()
        val splitItem = id.split(" to ")
        if (splitItem.size == 2) {
            val countingMessage = "counting... $id"
            this.log.info(countingMessage)
            returnCompleteTask.addLogs(countingMessage)

            var startNumber = splitItem[0].toInt()
            val endNumber = splitItem[1].toInt()

            while (startNumber < endNumber) {
                val logMessage = "$startNumber"
                returnCompleteTask.addLogs(logMessage)
                this.log.info(logMessage)
                startNumber += 20
            }
        }
        else {
            returnCompleteTask.isError = true
            returnCompleteTask.addLogs("improper key: $id")
        }

        return returnCompleteTask
    }
}
