package ui.screens

import fake.createProject
import fake.createUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.Audit
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
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
    private lateinit var sessionManager: SessionManagerUseCase
    @BeforeEach
    fun setup() {
        addProjectUseCase = mockk(relaxed = true)
        deleteProjectUseCase = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        updateProjectUseCase = mockk(relaxed = true)
        addAuditLogUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        projectScreen = ProjectManagementScreen(
            addProjectUseCase,
            deleteProjectUseCase,
            getAllProjectsUseCase,
            getProjectByIdUseCase,
            updateProjectUseCase,
            addAuditLogUseCase,
            consoleIO,
            sessionManager
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
    @Test
    fun `should update project when selecting option 3`() = runTest {
        // Given
        val existingProject = createProject()
        val updatedName = "Updated Name"
        val updatedDescription = "Updated Desc"
        val id = existingProject.id.toString()

        every { consoleIO.read() } returnsMany listOf("3", id, updatedName, updatedDescription, "0")
        coEvery { getProjectByIdUseCase.getProjectById(id) } returns existingProject
        every { sessionManager.getCurrentUser() } returns createUser()
        // When
        projectScreen.handleFeatureChoice()

        // Then
        coVerify {
            getProjectByIdUseCase.getProjectById(id)
            updateProjectUseCase.updateProject(match {
                it.id == existingProject.id &&
                        it.name == updatedName &&
                        it.description == updatedDescription &&
                        it.createdAt == existingProject.createdAt
            })
            addAuditLogUseCase.addAuditLog(match {
                it.userName == "defaultUser" &&
                        it.action == Audit.ActionType.UPDATE &&
                        it.entityType == Audit.EntityType.PROJECT &&
                        it.entityId == id
            })
        }

        verify {
            consoleIO.showWithLine("\u001B[32m✅ Project updated successfully.\u001B[0m")
        }
    }
    @Test
    fun `should delete project and log audit when selecting option 5`() = runTest {
        // Given
        val existingProject = createProject()
        val projectId = existingProject.id.toString()
        val mockUser = createUser()

        every { consoleIO.read() } returnsMany listOf("5", projectId, "0")

        coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns existingProject

        every { sessionManager.getCurrentUser() } returns mockUser

        // When
        projectScreen.handleFeatureChoice()

        // Then
        coVerify { deleteProjectUseCase.deleteProject(projectId) }
        coVerify {
            addAuditLogUseCase.addAuditLog(match {
                it.entityType == Audit.EntityType.PROJECT &&
                        it.action == Audit.ActionType.DELETE &&
                        it.entityId == projectId &&
                        it.userName == "defaultUser" &&
                        it.userRole == mockUser.role &&
                        it.actionDetails?.contains("deleted project with name") == true
            })
        }

        verify { consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m") }
    }
}
