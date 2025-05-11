package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAllTaskStatesUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var getAllTaskStatesUseCase: GetAllTaskStatesUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getAllTaskStatesUseCase = GetAllTaskStatesUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return all states successfully`() {
        runTest {
            // Given
            val createdBy = "adminUser01"
            val projects = listOf(
                createProject(name = "PlanMate Core Features", createdBy = createdBy),
                createProject(name = "PlanMate Extended", createdBy = createdBy)
            )
            val states = listOf(
                createState(name = "In Progress", projectId = projects[0].id),
                createState(name = "Done", projectId = projects[1].id)
            )

            coEvery { statesRepository.getAllTaskStates() } returns states

            // When
            val actualStates = getAllTaskStatesUseCase.getAllTaskStates()

            // Then
            assertThat(actualStates).isEqualTo(states)
        }
    }

    @Test
    fun `should return empty list when no states are found`() {
        runTest {
            // Given
            coEvery { statesRepository.getAllTaskStates() } returns emptyList()

            // When
            val actualStates = getAllTaskStatesUseCase.getAllTaskStates()

            // Then
            assertThat(actualStates).isEmpty()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return the correct states`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val states = listOf(
                createState(name = "In Progress", projectId = project[0].id),
                createState(name = "Done", projectId = project[0].id),
                createState(name = "Blocked", projectId = project[0].id)
            )

            coEvery { statesRepository.getAllTaskStates() } returns states

            // When
            val actualStates = getAllTaskStatesUseCase.getAllTaskStates()

            // Then
            assertThat(actualStates).isEqualTo(states)
        }
    }
}