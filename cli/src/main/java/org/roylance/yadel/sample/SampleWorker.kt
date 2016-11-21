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
        println("max memory: ${ManagementFactory.getMemoryMXBean().heapMemoryUsage.max / 1000000} MB")
    }

    override fun onReceive(message: Any?) {
        super.onReceive(message)

        if (message is YadelModel.Task) {
            if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
                Thread.sleep(1000)
                throw Exception("death and destruction!")
            }

            val completeTask = this.handleMessage(message.display)
            completeTask.task = message.toBuilder().addAllLogs(completeTask.task.logsList).build()
            completeTask(completeTask.build())

            if (random.nextBoolean()) {
                val randomInt = this.random.nextInt()
                val idAndDisplay = "$randomInt to ${randomInt + 100}"
                val newTask = YadelModel.Task.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDisplay(idAndDisplay)
                        .setDagId(message.dagId)

                val dependency = YadelModel.TaskDependency.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setParentTaskId(message.id)
                newTask.addDependencies(dependency)

                val addTask = YadelModel.AddTaskToDag
                        .newBuilder()
                        .setParentTask(message)
                        .setNewTask(newTask)

                this.getManagerSelection()?.tell(addTask.build(), this.self)
                this.log.info(addTask.toString())
            }
        }
    }

    private fun handleMessage(id: String): YadelModel.CompleteTask.Builder {
        val returnCompleteTask = YadelModel.CompleteTask.newBuilder()
        val returnCompleteTaskTask = YadelModel.Task.newBuilder()
        val splitItem = id.split(" to ")
        if (splitItem.size == 2) {
            val countingMessage = "counting... $id"
            this.log.info(countingMessage)
            returnCompleteTaskTask.addLogs(YadelModel.Log.newBuilder().setId(UUID.randomUUID().toString()).setMessage(countingMessage))

            var startNumber = splitItem[0].toInt()
            val endNumber = splitItem[1].toInt()

            while (startNumber < endNumber) {
                val logMessage = "$startNumber"
                returnCompleteTaskTask.addLogs(YadelModel.Log.newBuilder().setId(UUID.randomUUID().toString()).setMessage(logMessage))

                this.log.info(logMessage)
                startNumber += 20
            }
        }
        else {
            returnCompleteTask.isError = true
            returnCompleteTaskTask.addLogs(YadelModel.Log.newBuilder().setId(UUID.randomUUID().toString()).setMessage("improper key: $id"))
        }

        return returnCompleteTask.setTask(returnCompleteTaskTask)
    }
}
