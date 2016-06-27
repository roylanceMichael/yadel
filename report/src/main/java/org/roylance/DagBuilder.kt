package org.roylance

import org.roylance.yadel.api.models.YadelModels
import org.roylance.yadel.api.services.IBuilder
import java.util.*


class DagBuilder: IBuilder<YadelModels.Dag> {
    override fun build(): YadelModels.Dag {
        val dagToReturn = YadelModels.Dag.newBuilder()
        val dagDefinition = YadelModels.DagDefinition
                .newBuilder()
                .setId(UUID.randomUUID().toString())
                .setDisplay("amazing dag")

        val firstTask = buildTaskDefinition(FirstTaskId, dagDefinition)
        val secondTask = buildTaskDefinition(SecondTaskId, dagDefinition)
        val thirdTask = buildTaskDefinition(ThirdTaskId, dagDefinition)
        val fourthTask = buildTaskDefinition(FourthTaskId, dagDefinition)
        val fifthTask = buildTaskDefinition(FifthTaskId, dagDefinition)
        val sixthTask = buildTaskDefinition(SixthTaskId, dagDefinition)
        val seventhTask = buildTaskDefinition(SeventhTaskId, dagDefinition)
        val eigthTask = buildTaskDefinition(EigthTaskId, dagDefinition)
        val ninthTask = buildTaskDefinition(NinthTaskId, dagDefinition)
        val tenthTask = buildTaskDefinition(TenthTaskId, dagDefinition)

        tenthTask.mutableDependencies[NinthTaskId] = seventhTask.build()
        ninthTask.mutableDependencies[EigthTaskId] = sixthTask.build()
        eigthTask.mutableDependencies[SeventhTaskId] = fifthTask.build()
        seventhTask.mutableDependencies[SixthTaskId] = secondTask.build()
        sixthTask.mutableDependencies[FifthTaskId] = thirdTask.build()
        fifthTask.mutableDependencies[FourthTaskId] = fourthTask.build()
        fourthTask.mutableDependencies[ThirdTaskId] = firstTask.build()
        thirdTask.mutableDependencies[SecondTaskId] = firstTask.build()
        secondTask.mutableDependencies[FirstTaskId] = firstTask.build()

        dagDefinition.mutableFlattenedTasks[FirstTaskId] = firstTask.build()
        dagDefinition.mutableFlattenedTasks[SecondTaskId] = secondTask.build()
        dagDefinition.mutableFlattenedTasks[ThirdTaskId] = thirdTask.build()
        dagDefinition.mutableFlattenedTasks[FourthTaskId] = fourthTask.build()
        dagDefinition.mutableFlattenedTasks[FifthTaskId] = fifthTask.build()
        dagDefinition.mutableFlattenedTasks[SixthTaskId] = sixthTask.build()
        dagDefinition.mutableFlattenedTasks[SeventhTaskId] = seventhTask.build()
        dagDefinition.mutableFlattenedTasks[EigthTaskId] = eigthTask.build()
        dagDefinition.mutableFlattenedTasks[NinthTaskId] = ninthTask.build()
        dagDefinition.mutableFlattenedTasks[TenthTaskId] = tenthTask.build()

        dagToReturn.mutableUncompletedTasks[FirstTaskId] = buildTask(firstTask)
        dagToReturn.mutableUncompletedTasks[SecondTaskId] = buildTask(secondTask)
        dagToReturn.mutableUncompletedTasks[ThirdTaskId] = buildTask(thirdTask)
        dagToReturn.mutableUncompletedTasks[FourthTaskId] = buildTask(fourthTask)
        dagToReturn.mutableUncompletedTasks[FifthTaskId] = buildTask(fifthTask)
        dagToReturn.mutableUncompletedTasks[SixthTaskId] = buildTask(sixthTask)
        dagToReturn.mutableUncompletedTasks[SeventhTaskId] = buildTask(seventhTask)
        dagToReturn.mutableUncompletedTasks[EigthTaskId] = buildTask(eigthTask)
        dagToReturn.mutableUncompletedTasks[NinthTaskId] = buildTask(ninthTask)
        dagToReturn.mutableUncompletedTasks[TenthTaskId] = buildTask(tenthTask)

        dagToReturn.dagDefinition = dagDefinition.build()

        return dagToReturn.build()
    }


    private fun buildTask(taskDefinition: YadelModels.TaskDefinition.Builder): YadelModels.Task {
        return YadelModels.Task.newBuilder().setTaskDefinition(taskDefinition).build()
    }

    private fun buildTaskDefinition(id:String, dagDefinition: YadelModels.DagDefinition.Builder): YadelModels.TaskDefinition.Builder {
        return YadelModels.TaskDefinition.newBuilder()
                .setDagDefinition(dagDefinition)
                .setId(id)
                .setDisplay(id)
    }

    companion object {
        const val FirstTaskId = "0 to 100"
        const val SecondTaskId = "100 to 200"
        const val ThirdTaskId = "200 to 300"
        const val FourthTaskId = "300 to 400"
        const val FifthTaskId = "400 to 500"
        const val SixthTaskId = "500 to 600"
        const val SeventhTaskId = "600 to 700"
        const val EigthTaskId = "700 to 800"
        const val NinthTaskId = "800 to 900"
        const val TenthTaskId = "900 to 1000"
    }
}
