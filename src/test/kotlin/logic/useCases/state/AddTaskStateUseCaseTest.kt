package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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

class AddTaskStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var addTaskStateUseCase: AddTaskStateUseCase
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk()
        projectsRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectsRepository)
        addTaskStateUseCase = AddTaskStateUseCase(
            statesRepository,
            getAllProjectsUseCase,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return true when add valid new state`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val newState = createState(name = "Done", projectId = project[0].id.toString())

            coEvery { statesRepository.getAllStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns project
            coEvery { statesRepository.addState(newState) } returns true

            // When
            val result = addTaskStateUseCase.addState(newState)

            // Then
            assertThat(result).isTrue()
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when add invalid new state with empty name`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val newState = createState(name = "", projectId = project[0].id.toString())

            coEvery { statesRepository.getAllStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns project

            // When & Then
            assertThrows<InvalidStateNameException> {
                addTaskStateUseCase.addState(newState)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project does not exist`() {
        runTest {
            // Given
            val newState = createState(name = "Done", projectId = Uuid.random().toString())

            coEvery { statesRepository.getAllStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns listOf() // No matching project

            // When & Then
            assertThrows<ProjectNotFoundException> {
                addTaskStateUseCase.addState(newState)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when adding state that already exists`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val existingState = createState(projectId = project[0].id.toString())

            coEvery { statesRepository.getAllStates() } returns listOf(existingState)
            coEvery { getAllProjectsUseCase.getAllProjects() } returns project

            // When & Then
            assertThrows<StateAlreadyExistException> {
                addTaskStateUseCase.addState(existingState)
            }
        }

    }
}