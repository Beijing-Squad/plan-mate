package logic.useCases.task

import fake.createTask
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: AddTaskUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = AddTaskUseCase(repository)
    }

    @Test
    fun `addTask should call repository addTask`() = runTest {
        // Given
        val task = createTask()

        // When
        useCase.addTask(task)

        // Then
        coVerify { repository.addTask(task) }
    }
}