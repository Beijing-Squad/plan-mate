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
import kotlin.uuid.Uuid

class GetTaskStateByIdUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var getTaskStateByIdUseCase: GetTaskStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        getTaskStateByIdUseCase = GetTaskStateByIdUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return state when id exists`() {
        // Given
        val expectedState = createState(name = "Review", projectId = "550e8400-e29b-41d4-a716-446655440000")

        every { statesRepository.getStateById(expectedState.id.toString()) } returns expectedState

        // When
        val result = getTaskStateByIdUseCase.getStateById(expectedState.id.toString())

        // Then
        assertThat(result).isEqualTo(expectedState)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when state id does not exist`() {
        val state = createState(id = Uuid.parse("550e8400-e29b-41d4-a716-446655440000"))
        val states = listOf(
            createState(),
            createState()
        )

        every { statesRepository.getAllStates() } returns states
        every { statesRepository.getStateById(state.id.toString()) } throws StateNotFoundException()

        // When & Then
        assertThrows<StateNotFoundException> {
            getTaskStateByIdUseCase.getStateById(state.id.toString())
        }
    }
}