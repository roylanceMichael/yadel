package org.roylance.yadel.api.services

import org.roylance.common.service.IBuilder
import org.roylance.yadel.YadelModel

class CompleteTaskWrapper(
        val task: YadelModel.Task,
        val logger: ITaskLogger,
        val executable: IBuilder<Boolean>): IBuilder<YadelModel.CompleteTask> {
    override fun build(): YadelModel.CompleteTask {
        val completeTask = YadelModel.CompleteTask.newBuilder()
                .setTask(task.toBuilder().setFirstContextBase64(""))

        try {
            val result = executable.build()
            return completeTask
                    .setIsError(!result)
                    .setTask(completeTask.task.toBuilder().addAllLogs(logger.logs))
                    .build()
        }
        catch (e: Exception) {
            e.printStackTrace()
            logger.info(e.toString())

            return completeTask
                    .setIsError(true)
                    .setTask(completeTask.task.toBuilder().addAllLogs(logger.logs))
                    .build()
        }
    }
}