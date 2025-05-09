package logic.useCases.task

import com.google.common.truth.Truth.assertThat
import fake.createTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import logic.entities.exceptions.TaskNotFoundException
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

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
    fun `should return task when task id exists`() = runTest {
        // Given
        val task = createTask(
            projectId = "project-1",
            title = "Test Task",
            description = "Test Description",
            createdBy = "user-1",
            stateId = "state-1",
            createdAt = LocalDateTime(2023, 1, 1, 0, 0),
            updatedAt = LocalDateTime(2023, 1, 1, 0, 0)
        )
        coEvery { tasksRepository.getTaskById(task.id.toString()) } returns task

        // When
        val result = getTaskByIdUseCase.getTaskById(task.id.toString())

        // Then
        assertThat(result).isEqualTo(task)
        coVerify { tasksRepository.getTaskById(task.id.toString()) }
    }

    @Test
    fun `should throw TaskNotFoundException when task id does not exist`() = runTest {
        // Given
        val taskId = "no"
        coEvery { tasksRepository.getTaskById(taskId) } throws TaskNotFoundException("Task with ID $taskId not found")

        // When
        val exception = assertFailsWith<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }

        // Then
        assertThat(exception.message).isEqualTo("Task with ID $taskId not found")
        coVerify { tasksRepository.getTaskById(taskId) }
    }
}