package logic.useCases.task

import io.mockk.verify
import fake.createTask
import io.mockk.mockk
import logic.repository.TasksRepository
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import org.junit.jupiter.api.BeforeEach


class DeleteTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteTaskUseCase(repository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `deleteTask should call repository deleteTask`() {
        // Given
        val task = createTask()

        // When
        useCase.deleteTask(task.id.toString())

        // Then
        verify { repository.deleteTask(task.id.toString()) }
    }
}