
package logic.useCases.task

import com.google.common.truth.Truth.assertThat
import fake.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.entities.exceptions.InvalidInputException
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
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
    fun `should update task with new values`() {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDate(2023, 1, 1),
            updatedAt = LocalDate(2023, 1, 1)
        )
        val taskToUpdate = originalTask.copy(
            title = "Updated Title",
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.updateTask(taskToUpdate) } returns taskToUpdate

        // When
        val result = updateTaskUseCase.updateTask(taskToUpdate)

        // Then
        assertThat(result).isEqualTo(taskToUpdate)
        verify { tasksRepository.updateTask(taskToUpdate) }
    }

    @Test
    fun `should throw InvalidInputException when title is empty`() {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description"
        )
        val taskWithEmptyTitle = originalTask.copy(
            title = "",
            updatedAt = LocalDate(2023, 1, 2)
        )

        // When/Then
        val exception = assertFailsWith<InvalidInputException> {
            updateTaskUseCase.updateTask(taskWithEmptyTitle)
        }

        // Then
        assertThat(exception.message).isEqualTo("Task title cannot be empty")
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
    }

    @Test
    fun `should update task with new description`() {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDate(2023, 1, 1),
            updatedAt = LocalDate(2023, 2, 1)
        )
        val taskWithNewDescription = originalTask.copy(
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.updateTask(taskWithNewDescription) } returns taskWithNewDescription

        // When
        val result = updateTaskUseCase.updateTask(taskWithNewDescription)

        // Then
        assertThat(result).isEqualTo(taskWithNewDescription)
        verify { tasksRepository.updateTask(taskWithNewDescription) }
    }

    @Test
    fun `should update task with new title`() {
        // Given
        val originalTask = createTask(
            projectId = "project-1",
            title = "Original Title",
            description = "Original Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDate(2023, 1, 1),
            updatedAt = LocalDate(2023, 2, 1)
        )
        val taskWithNewTitle = originalTask.copy(
            title = "Updated Title",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.updateTask(taskWithNewTitle) } returns taskWithNewTitle

        // When
        val result = updateTaskUseCase.updateTask(taskWithNewTitle)

        // Then
        assertThat(result).isEqualTo(taskWithNewTitle)
        verify { tasksRepository.updateTask(taskWithNewTitle) }
    }
}