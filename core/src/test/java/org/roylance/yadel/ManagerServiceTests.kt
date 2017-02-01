package org.roylance.yadel

import org.junit.Test
import org.roylance.yadel.services.ManagerService
import org.roylance.yadel.services.memory.MemoryDagStore
import org.roylance.yadel.utilities.TestActor
import org.roylance.yadel.utilities.TestUtilities
import java.util.*

class ManagerServiceTests {
    @Test
    fun simpleAddDagTest() {
        // arrange
        val managerService = ManagerService(
                true,
                MemoryDagStore(),
                MemoryDagStore(),
                MemoryDagStore(),
                HashMap(),
                TestActor("report", TestUtilities.getSystemTaskLogger()),
                TestUtilities.getSystemTaskLogger())

        val dagId = UUID.randomUUID().toString()

        val newTask = YadelModel.Task.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setDisplay("test task")
                .setDagId(dagId)

        val newDag = YadelModel.Dag.newBuilder()
                .setId(dagId)
                .setDisplay("test display")
                .addFlattenedTasks(newTask)
                .addUncompletedTasks(newTask)
                .build()

        assert(managerService.activeDags().size() == 0)

        // act
        managerService.handleNewDag(newDag)

        // assert
        assert(managerService.activeDags().size() == 1)
    }

    @Test
    fun simpleEnsureActorsAreWorkingTest() {
        // arrange
        val managerService = ManagerService(
                true,
                MemoryDagStore(),
                MemoryDagStore(),
                MemoryDagStore(),
                HashMap(),
                TestActor("report", TestUtilities.getSystemTaskLogger()),
                TestUtilities.getSystemTaskLogger())

        val dagId = UUID.randomUUID().toString()

        val newTask = YadelModel.Task.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setDisplay("test task")
                .setDagId(dagId)

        val newDag = YadelModel.Dag.newBuilder()
                .setId(dagId)
                .setDisplay("test display")
                .addFlattenedTasks(newTask)
                .addUncompletedTasks(newTask)
                .build()

        assert(managerService.activeDags().size() == 0)
        managerService.handleNewDag(newDag)

        managerService.handleRegistration(TestUtilities.getTestActor())

        // act
        assert(managerService.activeDags().size() == 1)
        assert(managerService.activeDags().get(dagId)!!.uncompletedTasksCount == 1)
        assert(managerService.activeDags().get(dagId)!!.processingTasksCount == 0)
        managerService.handleEnsureWorkersWorking()
        managerService.handleTellWorkersToDoNewDagWork()

        // assert
        assert(managerService.activeDags().size() == 1)
        assert(managerService.activeDags().get(dagId)!!.uncompletedTasksCount == 0)
        assert(managerService.activeDags().get(dagId)!!.processingTasksCount == 1)
    }

}