package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.InvalidStateNameException
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var addStateUseCase: AddStateUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        addStateUseCase = AddStateUseCase(statesRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return true when add valid new state`() {
        // Given
        val adminRole = UserRole.ADMIN
        val project = createProject(
            name = "PlanMate Core Features", createdBy = "adminUser01"
        )
        val newState = createState(
            name = "Done", projectId = project.id.toString()
        )

        // when
        every { statesRepository.getAllStates() } returns listOf()
        every { statesRepository.addState(newState) } returns true
        val result = addStateUseCase.addState(newState, adminRole)

        //Then
        assertThat(result).isTrue()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when add invalid new state with empty name`() {
        // Given
        val adminRole = UserRole.ADMIN
        val errorMessage = "the name of the new state is empty"
        val project = createProject(
            name = "PlanMate Core Features", createdBy = "adminUser01"
        )
        val newState = createState(
            name = "", projectId = project.id.toString()
        )
        every { statesRepository.getAllStates() } returns listOf()
        every { statesRepository.addState(newState) } throws InvalidStateNameException(errorMessage)

        // When&Then
        assertThrows<InvalidStateNameException> {
            addStateUseCase.addState(newState, adminRole)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return false when add new state with not exist project id`() {
        //Given
        val adminRole = UserRole.ADMIN
        val project = listOf(
            createProject(
                name = "PlanMate Core Features", createdBy = "adminUser01"
            ), createProject(
                name = "PlanMate Core Features 2", createdBy = "adminUser02"
            )
        )
        val newState = createState(
            name = "Done", projectId = Uuid.random().toString()
        )


        // when
        every { statesRepository.getAllStates() } returns listOf()
        every { statesRepository.addState(newState) } returns false

        addStateUseCase.addState(newState, adminRole)

        //Then
        assertThat(newState.projectId).isNotIn(
            project.map { it.id.toString() }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when add new state already exist`() {
        // Given
        val adminRole = UserRole.ADMIN
        val errorMessage = "the new state is exist"
        val project = createProject(
            name = "PlanMate Core Features", createdBy = "adminUser01"
        )
        val newState = createState(
            projectId = project.id.toString()
        )
        every { statesRepository.getAllStates() } returns listOf(newState)
        every { statesRepository.addState(newState) } throws StateAlreadyExistException(errorMessage)

        // When&Then
        assertThrows<StateAlreadyExistException> {
            addStateUseCase.addState(newState, adminRole)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val mateRole = UserRole.MATE
        val errorMessage = "Sorry the user should be admin"
        val state = createState()

        every { statesRepository.addState(state) } throws StateUnauthorizedUserException(errorMessage)

        // When && Then
        assertThrows<StateUnauthorizedUserException> { addStateUseCase.addState(state, mateRole) }

    }
}