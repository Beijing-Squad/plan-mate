package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UpdateStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getStateByIdUseCase: GetStateByIdUseCase
    private lateinit var updateStateUseCase: UpdateStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getStateByIdUseCase = GetStateByIdUseCase(statesRepository)
        updateStateUseCase = UpdateStateUseCase(
            statesRepository,
            getStateByIdUseCase
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update state when state is exist`() {
        //Given
        val project = createProject()
        val state = createState(
            name = "in progress",
            projectId = project.id.toString()
        )
        val newState = createState(
            id = state.id,
            name = "done",
            projectId = state.projectId
        )

        every { getStateByIdUseCase.getStateById(newState.id) } returns state
        every { statesRepository.updateState(newState) } returns newState

        // when
        val result = updateStateUseCase.updateState(newState)

        //Then
        assertThat(result).isEqualTo(newState)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found state with this state id`() {
        // Given
        val newState = createState()

        every { statesRepository.getStateById(newState.id) } throws StateNotFoundException()

        // When & Then
        assertThrows<StateNotFoundException> {
            updateStateUseCase.updateState(newState)
        }
    }
}