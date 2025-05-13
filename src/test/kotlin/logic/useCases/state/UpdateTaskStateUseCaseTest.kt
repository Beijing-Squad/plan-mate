package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class UpdateTaskStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var updateTaskStateUseCase: UpdateTaskStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        updateTaskStateUseCase = UpdateTaskStateUseCase(
            statesRepository
        )
    }

    @Test
    fun `should update state when state is exist`() {
        runTest {
            //Given
            val project = createProject()
            val state = createState(
                name = "in progress",
                projectId = project.id
            )
            val newState = createState(
                id = state.id,
                name = "done",
                projectId = state.projectId
            )

            coEvery { statesRepository.updateTaskState(newState) } returns true

            // when
            val result = updateTaskStateUseCase.updateTaskState(newState)

            //Then
            assertThat(result).isTrue()
        }
    }

    @Test
    fun `should throw exception when id of new state not found`() {
        runTest {
            //Given
            val taskState = createState()
            coEvery { statesRepository.updateTaskState(taskState) } throws StateNotFoundException()

            //When & Then
            assertThrows<StateNotFoundException> {
                updateTaskStateUseCase.updateTaskState(taskState)
            }
        }
    }
}