package logic.useCases.state

import com.google.common.truth.Truth.assertThat
import fake.createProject
import fake.createState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.StateAlreadyExistException
import logic.repository.ProjectsRepository
import logic.repository.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AddTaskStateUseCaseTest {
    private lateinit var statesRepository: StatesRepository
    private lateinit var addTaskStateUseCase: AddTaskStateUseCase
    private lateinit var projectsRepository: ProjectsRepository

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        projectsRepository = mockk()
        addTaskStateUseCase = AddTaskStateUseCase(
            statesRepository
        )
    }

    @Test
    fun `should return true when add valid new state`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val newState = createState(name = "Done", projectId = project[0].id)

            coEvery { statesRepository.getAllTaskStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns project
            coEvery { statesRepository.addTaskState(newState) } returns true

            // When
            val result = addTaskStateUseCase.addTaskState(newState)

            // Then
            assertThat(result).isTrue()
        }

    }

    @Test
    fun `should return false when adding state fails in repository`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val newState = createState(name = "In Progress", projectId = project[0].id)

            coEvery { statesRepository.getAllTaskStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns project
            coEvery { statesRepository.addTaskState(newState) } returns false

            // When
            val result = addTaskStateUseCase.addTaskState(newState)

            // Then
            assertThat(result).isFalse()
        }
    }

    @Test
    fun `should not call addTaskState when repository throws an state already exist exception`() {
        runTest {
            // Given
            val project = listOf(
                createProject(name = "PlanMate Core Features", createdBy = "adminUser01")
            )
            val newState = createState(name = "On Hold", projectId = project[0].id)

            coEvery { statesRepository.addTaskState(newState) } throws StateAlreadyExistException()

            // When & Then
            assertThrows<Exception> {
                addTaskStateUseCase.addTaskState(newState)
            }
        }
    }

    @Test
    fun `should handle empty repository without throwing exception`() {
        runTest {
            // Given
            val newState = createState(name = "Ready", projectId = Uuid.random())

            coEvery { statesRepository.getAllTaskStates() } returns listOf()
            coEvery { projectsRepository.getAllProjects() } returns listOf()

            // When
            val result = addTaskStateUseCase.addTaskState(newState)

            // Then
            assertThat(result).isFalse()
        }
    }
}