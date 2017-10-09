package org.roylance.yadel.services

import org.roylance.common.service.IBuilder
import org.roylance.yadel.YadelModel

class CompleteTaskWrapper(
        val task: YadelModel.Task,
        val logger: ITaskLogger,
        val executable: IBuilder<Boolean>): IBuilder<YadelModel.CompleteTask> {
    override fun build(): YadelModel.CompleteTask {
        val completeTask = YadelModel.CompleteTask.newBuilder()
                .setTask(task.toBuilder())

        try {
            val result = executable.build()
            return completeTask
                    .setIsError(!result)
                    .setTask(completeTask.task.toBuilder()
                            .addAllLogs(logger.logs)
                            .clearFirstContextBase64()
                            .clearSecondContextBase64()
                            .clearThirdContextBase64())
                    .build()
        }
        catch (e: Exception) {
            e.printStackTrace()
            logger.info(e.toString())

            return completeTask
                    .setIsError(true)
                    .setTask(completeTask.task.toBuilder()
                            .addAllLogs(logger.logs)
                            .clearFirstContextBase64()
                            .clearSecondContextBase64()
                            .clearThirdContextBase64())
                    .build()
        }
    }
}