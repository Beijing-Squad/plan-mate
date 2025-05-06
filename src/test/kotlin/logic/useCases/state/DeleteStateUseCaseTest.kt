package logic.useCases.state

import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

class DeleteStateUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var deleteStateUseCase: DeleteStateUseCase
    private lateinit var getStateByIdUseCase: GetStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getStateByIdUseCase = GetStateByIdUseCase(statesRepository)
        deleteStateUseCase = DeleteStateUseCase(statesRepository, getStateByIdUseCase)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return true when state exists and deleted successfully`() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "1", name = "In Progress", projectId = project.id.toString())
        every { getStateByIdUseCase.getStateById(state.id) } returns state
        every { statesRepository.deleteState(state) } returns true

        // When
        val result = deleteStateUseCase.deleteState(state)

        // Then
        assertEquals(true, result)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when deleting non existent state`() {
        // Given
        val errorMessage = "the name of the new state is empty"
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "999", name = "Archived", projectId = project.id.toString())

        every { statesRepository.getAllStates() } returns listOf()
        every { statesRepository.getStateById(state.id) } throws StateNotFoundException(errorMessage)

        // When & Then
        assertThrows<StateNotFoundException> {
            deleteStateUseCase.deleteState(state)
        }
    }
}