package ui.screens

import fake.createProject
import io.mockk.*
import logic.useCases.project.*
import logic.entities.UserRole
import logic.useCases.audit.AddAuditLogUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
            consoleIO
        )
    }

    @Test
    fun `Given projects are available, when option 1 is selected, then all projects are displayed`() {
        // Given
        val project = listOf(createProject())
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects() } returns project

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine(match { it.contains("Project Management") })
            consoleIO.showWithLine(match { it.contains("defaultProjectName") })
        }
    }

    @Test
    fun `Given no projects are available, when option 1 is selected, then a warning message is displayed`() {
        // Given
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects() } returns emptyList()

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
        }
    }

    @Test
    fun `Given an error occurs while fetching projects, when option 1 is selected, then an error message is displayed`() {
        // Given
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects() } throws Exception("Error")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Error\u001B[0m")
        }
    }

    @Test
    fun `Given a valid project ID, when option 2 is selected, then the project is shown`() {
        // Given
        val id = Uuid.random()
        val project = createProject()
        every { consoleIO.read() } returnsMany listOf("2", id.toString())
        every { getProjectByIdUseCase.getProjectById(id.toString()) } returns project

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.show("\u001B[32mEnter project ID: \u001B[0m")
            consoleIO.showWithLine(match { it.contains(project.name) })
        }
    }

    @Test
    fun `Given an invalid project ID, when option 2 is selected, then an error message is displayed`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("2", "invalid-id")
        every { getProjectByIdUseCase.getProjectById("invalid-id") } throws Exception("Not found")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Not found\u001B[0m")
        }
    }

    @Test
    fun `Given a valid project, when option 3 is selected, then the project is updated successfully`() {
        // Given
        val project = createProject(name = "Old")
        val id = project.id
        every { consoleIO.read() } returnsMany listOf("3", id.toString(), "New name", "New desc")
        every { getProjectByIdUseCase.getProjectById(id.toString()) } returns project
        every { updateProjectUseCase.updateProject(any()) }

        // When
        projectScreen.execute()

        // Then
        verify {
            updateProjectUseCase.updateProject(match {
                it.id == id && it.name == "New name" && it.description == "New desc"
            })
            consoleIO.showWithLine(match { it.contains("✅ Project updated successfully") })
        }
    }

    @Test
    fun `Given a project to update is not found, when option 3 is selected, then an error message is displayed`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("3", "bad-id")
        every { getProjectByIdUseCase.getProjectById("bad-id") } throws Exception("Not found")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Project not found\u001B[0m")
        }
    }

    @Test
    fun `Given an error while updating project, when option 3 is selected, then an error message is displayed`() {
        // Given
        val project = createProject()
        val id = project.id
        every { consoleIO.read() } returnsMany listOf("3", id.toString(), "Name", "Desc")
        every { getProjectByIdUseCase.getProjectById(id.toString()) } returns project
        every { updateProjectUseCase.updateProject(any()) } throws Exception("Update failed")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Update failed\u001B[0m")
        }
    }

    @Test
    fun `Given valid project details, when option 4 is selected, then the project is added successfully`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("4", "Project Name", "Project Desc")
        every { addProjectUseCase.addProject(any()) }

        // When
        projectScreen.execute()

        // Then
        verify {
            addProjectUseCase.addProject(match {
                it.name == "Project Name" && it.description == "Project Desc"
            })
            consoleIO.showWithLine(match { it.contains("✅ Project added successfully") })
        }
    }

    @Test
    fun `Given an error while adding project, when option 4 is selected, then an error message is displayed`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("4", "Project", "Desc")
        every { addProjectUseCase.addProject(any()) } throws Exception("Add failed")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Add failed\u001B[0m")
        }
    }

    @Test
    fun `Given a valid project, when option 5 is selected, then the project is deleted successfully`() {
        // Given
        val project = createProject(name = "Delete Me")
        val id = project.id
        every { consoleIO.read() } returnsMany listOf("5", id.toString())
        every { getProjectByIdUseCase.getProjectById(id.toString()) } returns project
        every { deleteProjectUseCase.deleteProject(id.toString()) }

        // When
        projectScreen.execute()

        // Then
        verify {
            deleteProjectUseCase.deleteProject(id.toString())
            consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m")
        }
    }

    @Test
    fun `Given an error while deleting project, when option 5 is selected, then an error message is displayed`() {
        // Given
        val id = Uuid.random().toString()
        every { consoleIO.read() } returnsMany listOf("5", id)
        every { deleteProjectUseCase.deleteProject(id) } throws Exception("Delete failed")

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Delete failed\u001B[0m")
        }
    }
}
