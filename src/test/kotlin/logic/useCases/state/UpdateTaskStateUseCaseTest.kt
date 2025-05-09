package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UpdateTaskStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getTaskStateByIdUseCase: GetTaskStateByIdUseCase
    private lateinit var updateTaskStateUseCase: UpdateTaskStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getTaskStateByIdUseCase = GetTaskStateByIdUseCase(statesRepository)
        updateTaskStateUseCase = UpdateTaskStateUseCase(
            statesRepository,
            getTaskStateByIdUseCase
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update state when state is exist`() {
        runTest {
            //Given
            val project = createProject()
            val state = createState(
                name = "in progress",
                projectId = project.id.toString()
            )
            val newState = createState(
                id = state.id,
                name = "done",
                projectId = state.projectId.toString()
            )

            coEvery { getTaskStateByIdUseCase.getStateById(newState.id.toString()) } returns state
            coEvery { statesRepository.updateState(newState) } returns newState

            // when
            val result = updateTaskStateUseCase.updateState(newState)

            //Then
            assertThat(result).isEqualTo(newState)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found state with this state id`() {
        runTest {
            // Given
            val newState = createState()

            coEvery { statesRepository.getStateById(newState.id.toString()) } throws StateNotFoundException()

            // When & Then
            assertThrows<StateNotFoundException> {
                updateTaskStateUseCase.updateState(newState)
            }
        }
    }
}