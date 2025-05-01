package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.StateNotFoundException
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UpdateStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var updateStateUseCase: UpdateStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        updateStateUseCase = UpdateStateUseCase(statesRepository)
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

        every { statesRepository.getStateById(newState.id.toString()) } returns state
        every { statesRepository.updateState(newState) } returns true

        // when
        val result = updateStateUseCase.updateState(newState)

        //Then
        assertThat(result).isTrue()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found state with this state id`() {
        // Given
        val errorMessage = "Not found state with this id to update"
        val newState = createState()

        every { statesRepository.getStateById(newState.id.toString()) } throws StateNotFoundException(errorMessage)
        every { statesRepository.updateState(newState) } throws StateNotFoundException(errorMessage)

        // When & Then
        assertThrows<StateNotFoundException> {
            updateStateUseCase.updateState(newState)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val errorMessage = "Sorry the user should be admin"
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

        every { statesRepository.getStateById(state.id.toString()) } returns state
        every { statesRepository.updateState(newState) } throws StateUnauthorizedUserException(errorMessage)

        // When && Then
        assertThrows<StateUnauthorizedUserException> {
            updateStateUseCase.updateState(newState)
        }

    }
}