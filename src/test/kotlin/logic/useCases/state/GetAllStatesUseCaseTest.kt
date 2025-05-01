package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetAllStatesUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var getAllStatesUseCase: GetAllStatesUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getAllStatesUseCase = GetAllStatesUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun shouldReturnAllStatesSuccessfully() {
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
        val actualStates = getAllStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates).isEqualTo(states)
    }

    @Test
    fun shouldReturnEmptyListWhenNoStatesExist() {
        // Given
        every { statesRepository.getAllStates() } returns emptyList()

        // When
        val actualStates = getAllStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates).isEmpty()
    }

    @Test
    fun shouldReturnCorrectNumberOfStates() {
        // Given
        val states = listOf(
            createState(id = "1", name = "In Progress", projectId = "1"),
            createState(id = "2", name = "Done", projectId = "2"),
            createState(id = "3", name = "Blocked", projectId = "3")
        )

        every { statesRepository.getAllStates() } returns states

        // When
        val actualStates = getAllStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates.size).isEqualTo(3)
    }

    @Test
    fun shouldNotReturnNullWhenRepositoryReturnsEmptyList() {
        // Given
        every { statesRepository.getAllStates() } returns emptyList()

        // When
        val actualStates = getAllStatesUseCase.getAllStates()

        // Then
        assertThat(actualStates).isNotNull()
    }
}
