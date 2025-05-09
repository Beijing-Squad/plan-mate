package logic.useCases.task

import fake.createTask
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class DeleteTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `deleteTask should call repository deleteTask`() = runTest {
        // Given
        val task = createTask()

        // When
        useCase.deleteTask(task.id.toString())

        // Then
        coVerify { repository.deleteTask(task.id.toString()) }
    }
}