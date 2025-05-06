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

class DeleteTaskStateUseCaseTest {

    private lateinit var statesRepository: StatesRepository
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase
    private lateinit var getTaskStateByIdUseCase: GetTaskStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getTaskStateByIdUseCase = GetTaskStateByIdUseCase(statesRepository)
        deleteTaskStateUseCase = DeleteTaskStateUseCase(statesRepository, getTaskStateByIdUseCase)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return true when state exists and deleted successfully`() {
        // Given
        val project = createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        val state = createState(id = "1", name = "In Progress", projectId = project.id.toString())
        every { getTaskStateByIdUseCase.getStateById(state.id) } returns state
        every { statesRepository.deleteState(state) } returns true

        // When
        val result = deleteTaskStateUseCase.deleteState(state)

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
            deleteTaskStateUseCase.deleteState(state)
        }
    }
}