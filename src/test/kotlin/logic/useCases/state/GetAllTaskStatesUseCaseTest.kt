package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
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
        // Given
        val createdBy = "adminUser01"
        val projects = listOf(
            createProject(name = "PlanMate Core Features", createdBy = createdBy),
            createProject(name = "PlanMate Extended", createdBy = createdBy)
        )
        val states = listOf(
            createState(id = "1", name = "In Progress", projectId = projects[0].id.toString()),
            createState(id = "2", name = "Done", projectId = projects[1].id.toString())
        )

        every { statesRepository.getAllStates() } returns states

        // When
        val actualStates = getAllTaskStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates).isEqualTo(states)
    }

    @Test
    fun `should throw exception when not found states`() {
        // Given
        every { statesRepository.getAllStates() } returns emptyList()

        // When && Then
        assertThrows<StateNotFoundException> {
            getAllTaskStatesUseCase.getAllStates()
        }
    }

    @Test
    fun `should return the correct states`() {
        // Given
        val states = listOf(
            createState(id = "1", name = "In Progress", projectId = "1"),
            createState(id = "2", name = "Done", projectId = "2"),
            createState(id = "3", name = "Blocked", projectId = "3")
        )

        every { statesRepository.getAllStates() } returns states

        // When
        val actualStates = getAllTaskStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates).isEqualTo(states)
    }
}