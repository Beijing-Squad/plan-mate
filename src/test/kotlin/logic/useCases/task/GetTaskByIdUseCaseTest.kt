package logic.useCases.task

import com.google.common.truth.Truth.assertThat
import fake.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskByIdUseCaseTest {

    private lateinit var tasksRepository: TasksRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)
    }

    @Test
    fun `should return task when task id exists`() {
        // Given
        val task = createTask(
            projectId = "project-1",
            title = "Test Task",
            description = "Test Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDate(2023, 1, 1),
            updatedAt = LocalDate(2023, 1, 1)
        )
        every { tasksRepository.getTaskById(task.id.toString()) } returns task

        // When
        val result = getTaskByIdUseCase.getTaskById(task.id.toString())

        // Then
        assertThat(result).isEqualTo(task)
        verify { tasksRepository.getTaskById(task.id.toString()) }
    }

    @Test
    fun `should throw TaskNotFoundException when task id does not exist`() {
        // Given
        val taskId = "no"
        every { tasksRepository.getTaskById(taskId) } returns null

        // When/Then
        val exception = assertFailsWith<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }

        // Then
        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        verify { tasksRepository.getTaskById(taskId) }
    }
}