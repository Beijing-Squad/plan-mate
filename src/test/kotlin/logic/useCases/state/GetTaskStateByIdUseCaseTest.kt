package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetTaskStateByIdUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getTaskStateByIdUseCase: GetTaskStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        getTaskStateByIdUseCase = GetTaskStateByIdUseCase(statesRepository)
    }

    @Test
    fun `should return state when id exists`() {
        // Given
        val expectedState = createState(id = "456", name = "Review", projectId = "project-2")

        every { statesRepository.getStateById("456") } returns expectedState

        // When
        val result = getTaskStateByIdUseCase.getStateById(stateId = "456")

        // Then
        assertThat(result).isEqualTo(expectedState)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when state id does not exist`() {
        val state = createState(id = "999")
        val states = listOf(
            createState(),
            createState()
        )

        every { statesRepository.getAllStates() } returns states
        every { statesRepository.getStateById(state.id) } throws StateNotFoundException()

        // When & Then
        assertThrows<StateNotFoundException> {
            getTaskStateByIdUseCase.getStateById(state.id)
        }
    }
}