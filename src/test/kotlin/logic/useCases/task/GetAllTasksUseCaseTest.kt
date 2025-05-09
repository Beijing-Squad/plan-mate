package logic.useCases.task

import com.google.common.truth.Truth.assertThat
import fake.createTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllTasksUseCaseTest {

    private lateinit var tasksRepository: TasksRepository
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        getAllTasksUseCase = GetAllTasksUseCase(tasksRepository)
    }

    @Test
    fun `should return all tasks when multiple tasks exist`() = runTest {
        // Given
        val task1 = createTask(
            projectId = "project-1",
            title = "Task 1",
            description = "Description 1",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val task2 = createTask(
            projectId = "project-2",
            title = "Task 2",
            description = "Description 2",
            createdBy = "user-2",
            stateId = "state-2",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val tasks = listOf(task1, task2)
        coEvery { tasksRepository.getAllTasks() } returns tasks

        // When
        val result = getAllTasksUseCase.getAllTasks()

        // Then
        assertThat(result).containsExactly(task1, task2)
        coVerify { tasksRepository.getAllTasks() }
    }

    @Test
    fun `should return empty list when no tasks exist`() = runTest {
        // Given
        coEvery { tasksRepository.getAllTasks() } returns emptyList()

        // When
        val result = getAllTasksUseCase.getAllTasks()

        // Then
        assertThat(result).isEmpty()
        coVerify { tasksRepository.getAllTasks() }
    }

    @Test
    fun `should return single task when only one task exists`() = runTest {
        // Given
        val task = createTask(
            projectId = "project-1",
            title = "Single Task",
            description = "Single Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        coEvery { tasksRepository.getAllTasks() } returns listOf(task)

        // When
        val result = getAllTasksUseCase.getAllTasks()

        // Then
        assertThat(result).containsExactly(task)
        coVerify { tasksRepository.getAllTasks() }
    }
}