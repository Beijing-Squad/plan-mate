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
import logic.useCases.task.UpdateTaskUseCase
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
      //  every { tasksRepository.getTaskById(taskId) } returns null

        // When/Then
        val exception = assertFailsWith<TaskNotFoundException> {
            updateTaskUseCase.updateTask(taskId, title = "Updated Title", description = null, currentDate = LocalDate(2023, 1, 2))
        }

        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        verify { tasksRepository.getTaskById(taskId) }
        verify(exactly = 0) { tasksRepository.updateTask(any(), any()) }
    }

    @Test
    fun `should update task title when new title is provided`() {
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
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) } returns updatedTask

        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = "Updated Title",
            description = null,
            currentDate = LocalDate(2023, 1, 2)
        )

        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) }
    }

    @Test
    fun `should update task description when new description is provided`() {
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
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) } returns updatedTask

        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = null,
            description = "Updated Description",
            currentDate = LocalDate(2023, 1, 2)
        )

        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) }
    }

    @Test
    fun `should update both title and description when both are provided`() {
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
            description = "Updated Description",
            updatedAt = LocalDate(2023, 1, 2)
        )

        every { tasksRepository.getTaskById(originalTask.id.toString()) } returns originalTask
        every { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) } returns updatedTask

        val result = updateTaskUseCase.updateTask(
            taskId = originalTask.id.toString(),
            title = "Updated Title",
            description = "Updated Description",
            currentDate = LocalDate(2023, 1, 2)
        )

        assertThat(result).isEqualTo(updatedTask)
        verify { tasksRepository.getTaskById(originalTask.id.toString()) }
        verify { tasksRepository.updateTask(originalTask.id.toString(), updatedTask) }
    }

    @Test
    fun `should throw InvalidInputException when title is empty`() {
        val taskId = "any-id"
        every { tasksRepository.getTaskById(taskId) } returns createTask(taskId)

        val exception = assertFailsWith<InvalidInputException> {
            updateTaskUseCase.updateTask(
                taskId = taskId,
                title = "",
                description = null,
                currentDate = LocalDate(2023, 1, 2)
            )
        }

        assertThat(exception.message).isEqualTo("Task title cannot be empty")
        verify(exactly = 0) { tasksRepository.updateTask(any(), any()) }
    }

    @Test
    fun `should throw InvalidInputException when description is empty`() {
        val taskId = "any-id"
        every { tasksRepository.getTaskById(taskId) } returns createTask( taskId)

        val exception = assertFailsWith<InvalidInputException> {
            updateTaskUseCase.updateTask(
                taskId = taskId,
                title = null,
                description = "",
                currentDate = LocalDate(2023, 1, 2)
            )
        }

        assertThat(exception.message).isEqualTo("Task description cannot be empty")
        verify(exactly = 0) { tasksRepository.updateTask(any(), any()) }
    }
}
