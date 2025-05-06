package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.InvalidStateNameException
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.StateAlreadyExistException
import logic.repository.ProjectsRepository
import logic.repository.StatesRepository
import logic.useCases.project.GetAllProjectsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var addStateUseCase: AddStateUseCase
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        projectsRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectsRepository)
        addStateUseCase = AddStateUseCase(
            statesRepository,
            getAllProjectsUseCase,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return true when add valid new state`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val newState = createState(name = "Done", projectId = project[0].id.toString())

        every { statesRepository.getAllStates() } returns listOf()
        every { projectsRepository.getAllProjects() } returns project
        every { statesRepository.addState(newState) } returns true

        // When
        val result = addStateUseCase.addState(newState)

        // Then
        assertThat(result).isTrue()
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when add invalid new state with empty name`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val newState = createState(name = "", projectId = project[0].id.toString())

        every { statesRepository.getAllStates() } returns listOf()
        every { projectsRepository.getAllProjects() } returns project

        // When & Then
        assertThrows<InvalidStateNameException> {
            addStateUseCase.addState(newState)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project does not exist`() {
        // Given
        val newState = createState(name = "Done", projectId = Uuid.random().toString())

        every { statesRepository.getAllStates() } returns listOf()
        every { projectsRepository.getAllProjects() } returns listOf() // No matching project

        // When & Then
        assertThrows<ProjectNotFoundException> {
            addStateUseCase.addState(newState)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when adding state that already exists`() {
        // Given
        val project = listOf(
            createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
        )
        val existingState = createState(projectId = project[0].id.toString())

        every { statesRepository.getAllStates() } returns listOf(existingState)
        every { getAllProjectsUseCase.getAllProjects() } returns project

        // When & Then
        assertThrows<StateAlreadyExistException> {
            addStateUseCase.addState(existingState)
        }
    }
}