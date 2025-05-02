package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
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
        statesRepository = mockk(relaxed = true)
        updateStateUseCase = UpdateStateUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update state when state is exist`() {
        //Given
        val adminRole = UserRole.ADMIN
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

        every { statesRepository.getStateById(newState.id) } returns state
        every { statesRepository.updateState(newState) } returns newState

        // when
        val result = updateStateUseCase.updateState(newState, adminRole)

        //Then
        assertThat(result).isEqualTo(newState)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found state with this state id`() {
        // Given
        val errorMessage = "not found this state"
        val adminRole = UserRole.ADMIN
        val newState = createState()

        every { statesRepository.getStateById(newState.id) } returns null

        // When & Then
        assertThrows<StateNotFoundException> {
            updateStateUseCase.updateState(newState, adminRole)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val mateRole = UserRole.MATE
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

        // When && Then
        assertThrows<StateUnauthorizedUserException> {
            updateStateUseCase.updateState(newState, mateRole)
        }
    }
}