package ui.screens

import fake.createProject
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.UserRole
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.project.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ProjectManagementScreenTest {

    private lateinit var addProjectUseCase: AddProjectUseCase
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var projectScreen: ProjectManagementScreen
    private lateinit var addAuditLogUseCase: AddAuditLogUseCase
    private val userRole: UserRole = UserRole.ADMIN

    @BeforeEach
    fun setup() {
        addProjectUseCase = mockk(relaxed = true)
        deleteProjectUseCase = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        updateProjectUseCase = mockk(relaxed = true)
        addAuditLogUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        projectScreen = ProjectManagementScreen(
            addProjectUseCase,
            deleteProjectUseCase,
            getAllProjectsUseCase,
            getProjectByIdUseCase,
            updateProjectUseCase,
            addAuditLogUseCase,
            userRole,
            consoleIO,
        )
    }

    @Test
    fun `list all projects when projects available`() {
        runTest {
            // Given
            val project = listOf(createProject())
            every { consoleIO.read() } returnsMany listOf("1", "0")
            coEvery { getAllProjectsUseCase.getAllProjects() } returns project

            // When
            projectScreen.handleFeatureChoice()

            // Then
            verify {
                consoleIO.showWithLine(match { it.contains("defaultProjectName") })
            }
        }
    }

    @Test
    fun `list all projects empty when no projects`() {
        runTest {
            // Given
            every { consoleIO.read() } returnsMany listOf("1", "0")
            coEvery { getAllProjectsUseCase.getAllProjects() } returns emptyList()

            // When
            projectScreen.handleFeatureChoice()

            // Then
            verify {
                consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
            }
        }
    }

    @Test
    fun `should add new project when selecting option 4`() {
        runTest {
            // Given
            every { consoleIO.read() } returnsMany listOf("4", "New Project", "Description", "creatorUser", "0")

            // When
            projectScreen.handleFeatureChoice()

            // Then
            coVerify {
                addProjectUseCase.addProject(match { it.name == "New Project" && it.description == "Description" })
                addAuditLogUseCase.addAuditLog(any())
            }
        }
    }

    @Test
    fun `should get new project when selecting option 2`() {
        runTest {
            // Given
            val project = createProject()
            val projectId = project.id
            every { consoleIO.read() } returnsMany listOf("2", "projectId", "0")
            coEvery { getProjectByIdUseCase.getProjectById(projectId.toString()) } returns project

            // When
            projectScreen.handleFeatureChoice()

            // Then
            verify {
                consoleIO.showWithLine(match { it.contains("Find Project by ID") })
            }
        }
    }

    @Test
    fun `invalid option shows error`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("invalid", "0")
        // When
        projectScreen.handleFeatureChoice()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")
        }
    }
}
