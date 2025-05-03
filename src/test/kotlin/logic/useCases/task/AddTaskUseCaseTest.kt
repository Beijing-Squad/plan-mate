package logic.useCases.task

import io.mockk.verify
import fake.createTask
import io.mockk.mockk
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class AddTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: AddTaskUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = AddTaskUseCase(repository)
    }


    @Test
    fun `addTask should call repository addTask`() {
        // Given
        val task = createTask()

        // When
        useCase.addTask(task)

        // Then
        verify { repository.addTask(task) }
    }
}
