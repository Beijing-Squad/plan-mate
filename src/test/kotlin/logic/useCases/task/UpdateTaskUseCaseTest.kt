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
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateTaskUseCaseTest {

    private lateinit var tasksRepository: TasksRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        updateTaskUseCase = UpdateTaskUseCase(tasksRepository)
    }

    @Test
    fun `should update task with new values`() = runTest {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val taskToUpdate = originalTask.copy(
            title = "Updated Title",
            description = "Updated Description",
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )

        coEvery { tasksRepository.updateTask(taskToUpdate) } returns taskToUpdate

        // When
        val result = updateTaskUseCase.updateTask(taskToUpdate)

        // Then
        assertThat(result).isEqualTo(taskToUpdate)
        coVerify { tasksRepository.updateTask(taskToUpdate) }
    }


    @Test
    fun `should update task with new description`() = runTest {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val taskWithNewDescription = originalTask.copy(
            description = "Updated Description",
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )

        coEvery { tasksRepository.updateTask(taskWithNewDescription) } returns taskWithNewDescription

        // When
        val result = updateTaskUseCase.updateTask(taskWithNewDescription)

        // Then
        assertThat(result).isEqualTo(taskWithNewDescription)
        coVerify { tasksRepository.updateTask(taskWithNewDescription) }
    }

    @Test
    fun `should update task with new title`() = runTest {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        val taskWithNewTitle = originalTask.copy(
            title = "Updated Title",
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )

        coEvery { tasksRepository.updateTask(taskWithNewTitle) } returns taskWithNewTitle

        // When
        val result = updateTaskUseCase.updateTask(taskWithNewTitle)

        // Then
        assertThat(result).isEqualTo(taskWithNewTitle)
        coVerify { tasksRepository.updateTask(taskWithNewTitle) }
    }
}