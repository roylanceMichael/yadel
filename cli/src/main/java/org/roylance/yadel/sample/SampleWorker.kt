package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.WorkerBase
import org.roylance.yadel.api.models.YadelModels
import java.util.*

class SampleWorker:WorkerBase() {
    private val random = Random()

    override fun onReceive(p0: Any?) {
        super.onReceive(p0)

        if (p0 is YadelModels.Task) {
            val completeTask = this.handleMessage(p0.taskDefinition.id)
            completeTask.taskDefinition = p0.taskDefinition
            this.completeTask(completeTask.build())

            if (random.nextBoolean()) {
                val randomInt = this.random.nextInt()
                val idAndDisplay = "$randomInt to ${randomInt + 100}"
                val newTask = YadelModels.TaskDefinition.newBuilder()
                        .setId(idAndDisplay)
                        .setDisplay(idAndDisplay)
                        .setDagDefinition(p0.taskDefinition.dagDefinition)

                newTask.mutableDependencies[p0.taskDefinition.id] = p0.taskDefinition

                val addTask = YadelModels.AddTaskToDag
                        .newBuilder()
                        .setParentTask(p0.taskDefinition)
                        .setNewTask(newTask)

                this.getManagerSelection()?.tell(addTask.build(), this.self)
                this.log.info("${addTask.toString()}")
            }
        }
    }

    private fun handleMessage(id:String):YadelModels.CompleteTask.Builder {
        val returnCompleteTask = YadelModels.CompleteTask.newBuilder()
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
                startNumber++
                Thread.sleep(500)
            }
        }
        else {
            returnCompleteTask.isError = true
            returnCompleteTask.addLogs("improper key: $id")
        }

        return returnCompleteTask
    }
}
