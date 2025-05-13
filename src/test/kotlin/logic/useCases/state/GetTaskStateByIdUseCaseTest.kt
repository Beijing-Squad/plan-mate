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
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val expectedState = createState(name = "Review", projectId = project[0].id)

            coEvery { statesRepository.getTaskStateById(expectedState.id) } returns expectedState

            // When
            val result = getTaskStateByIdUseCase.getTaskStateById(expectedState.id)

            // Then
            assertThat(result).isEqualTo(expectedState)
        }
    }

    @Test
    fun `should throw exception when state id does not exist`() {
        runTest {
            val state = createState(id = Uuid.parse("550e8400-e29b-41d4-a716-446655440000"))
            val states = listOf(
                createState(),
                createState()
            )

            coEvery { statesRepository.getAllTaskStates() } returns states
            coEvery { statesRepository.getTaskStateById(state.id) } throws StateNotFoundException()

            // When & Then
            assertThrows<StateNotFoundException> {
                getTaskStateByIdUseCase.getTaskStateById(state.id)
            }
        }
    }
}