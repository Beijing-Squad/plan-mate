package logic.useCases.task

import com.google.common.truth.Truth.assertThat
import fake.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.TaskNotFoundException
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
    fun `should throw TaskNotFoundException when updating non-existent task`() {
        // Given
        val taskId = "not"
        every { tasksRepository.getTaskById(taskId) } throws TaskNotFoundException("Task with ID $taskId not found")

        // When/Then
        val exception = assertFailsWith<TaskNotFoundException> {
            updateTaskUseCase.updateTask(
                taskId, title = "Updated Title",
                description = "updated Description",
                currentDate = LocalDate(2023, 1, 2)
            )
        }

        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        verify { tasksRepository.getTaskById(taskId) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
    }

    @Test
    fun `should update task title when new title is provided`() {
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
        val updatedTask = originalTask.copy(
            title = "Updated Title",
            description = "update",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask( updatedTask) } returns updatedTask
        // When
        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = "Updated Title",
            description = "update",
            currentDate = LocalDate(2023, 1, 2)
        )
        // Then
        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(updatedTask) }
    }

    @Test
    fun `should update task description when new description is provided`() {
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
        val updatedTask = originalTask.copy(
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask( updatedTask) } returns updatedTask
        // When

        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = null,
            description = "Updated Description",
            currentDate = LocalDate(2023, 1, 2)
        )
        // Then
        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(updatedTask) }
    }

    @Test
    fun `should update both title and description when both are provided`() {
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
        val updatedTask = originalTask.copy(
            title = "Updated Title",
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask( updatedTask) } returns updatedTask
        // When
        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = "Updated Title",
            description = "Updated Description",
            currentDate = LocalDate(2023, 1, 2)
        )
        // Then
        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(updatedTask) }
    }

    @Test
    fun `should throw InvalidInputException when title is empty`() {
        // Given
        val taskId = "any-id"
        every { tasksRepository.getTaskById(taskId) } returns createTask(taskId)
        // When
        val exception = assertFailsWith<InvalidInputException> {
            updateTaskUseCase.updateTask(
                taskId = taskId,
                title = "",
                description = "updated",
                currentDate = LocalDate(2023, 1, 2)
            )
        }
        // Then
        assertThat(exception.message).isEqualTo("Task title cannot be empty")
        verify(exactly = 0) { tasksRepository.updateTask( any()) }
    }

    @Test
    fun `should keep original description when new description is null`() {
        // Given
        val originalTask = createTask(description = "Original description")
        val taskId = originalTask.id.toString()
        every { tasksRepository.getTaskById(taskId) } returns originalTask
        every { tasksRepository.updateTask(any()) } answers { firstArg() }

        // When
        val updated = updateTaskUseCase.updateTask(
            taskId = taskId,
            title = "updated title",
            description = null,
            currentDate = originalTask.updatedAt
        )

        // Then
        assertThat(updated.description).isEqualTo("Original description")
    }
}
