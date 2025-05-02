package ui.screens

import fake.createProject
import io.mockk.*
import logic.useCases.project.*
import logic.entities.UserRole
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
    private  val userRole: UserRole = UserRole.ADMIN

    @BeforeEach
    fun setup() {
        addProjectUseCase = mockk(relaxed = true)
        deleteProjectUseCase = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        updateProjectUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        projectScreen = ProjectManagementScreen(
            addProjectUseCase,
            deleteProjectUseCase,
            getAllProjectsUseCase,
            getProjectByIdUseCase,
            updateProjectUseCase,
            userRole,
            consoleIO
        )
    }

    @Test
    fun `should show option when navigate to user screen`() {
        // Given && When
        projectScreen.showOptionService()

        // Then
        verify {
            consoleIO.showWithLine(any())
        }
    }

    @Test
    fun `should show invalid option message when selecting unknown option`() {
        //Given
        every { consoleIO.read() } returns "invalid"

        //When
        projectScreen.execute()

        //Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")
        }
    }

    @Test
    fun `should exit to main menu when selecting option 0`(){
        //Given
        every { consoleIO.read() } returns "0"

        //When
        projectScreen.execute()

        //Then
        verify {
            consoleIO.showWithLine(match { it.contains("Project Management") })
        }
        // No further interactions (e.g., no project operations or error messages)
        verify(exactly = 0) {
            consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")
            getAllProjectsUseCase.getAllProjects(any())
            getProjectByIdUseCase.getProjectById(any(), any())
            addProjectUseCase.addProject(any(), any())
            updateProjectUseCase.updateProject(any(), any())
            deleteProjectUseCase.deleteProject(any(), any())
        }
    }

    @Test
    fun `should get all projects and display them when selecting option 1`() {
        //Given
        val project = listOf(createProject())
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects(any()) } returns Result.success(project)

        //When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine(match { it.contains("Project Management") })
            consoleIO.showWithLine(match { it.contains("defaultProjectName") })
        }
    }
    @Test
    fun `should show warning when no projects found`() {
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects(any()) } returns Result.success(emptyList())

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
        }
    }

    @Test
    fun `should show error when getAllProjects fails`() {
        every { consoleIO.read() } returns "1"
        every { getAllProjectsUseCase.getAllProjects(any()) } returns Result.failure(Exception("Error"))

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[31m❌ Error\u001B[0m")
        }
    }

    @Test
    fun `should ask for the project ID and show the project if it exists when selecting option 2`() {
        // Given
        val id = Uuid.random()
        val project = createProject()
        every { consoleIO.read() } returnsMany listOf("2", id.toString())
        every { getProjectByIdUseCase.getProjectById(id.toString(), UserRole.ADMIN) } returns Result.success(project)
        every { consoleIO.showWithLine(any()) } just Runs

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.show("\u001B[32mEnter project ID: \u001B[0m")
            consoleIO.showWithLine(match { it.contains(project.name) })
        }
    }

    @Test
    fun `should show error when project by ID not found`() {
        every { consoleIO.read() } returnsMany listOf("2", "non-existent-id")
        every { getProjectByIdUseCase.getProjectById("non-existent-id", UserRole.ADMIN) } returns Result.failure(Exception("Not found"))

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[31m❌ Not found\u001B[0m")
        }
    }

    @Test
    fun `should update project if id exists when selecting option 3`() {
        //Given
        val project = createProject(name = "Old project")
        val id = project.id
        every { consoleIO.read() } returnsMany listOf("3", id.toString(), "New project name", "New desc")
        every { getProjectByIdUseCase.getProjectById(id.toString(), UserRole.ADMIN) } returns Result.success(project)
        every { updateProjectUseCase.updateProject(any(), UserRole.ADMIN) } returns Result.success(true)

        //When
        projectScreen.execute()

        //Given
        verify {
            updateProjectUseCase.updateProject(match {
                it.id == id &&
                        it.name == "New project name" &&
                        it.description == "New desc"
            }, UserRole.ADMIN)
        }

        verify {
            consoleIO.showWithLine(match { it.contains("✅ Project updated successfully") })
        }
    }

    @Test
    fun `should show error when project to update not found`() {
        every { consoleIO.read() } returnsMany listOf("3", "bad-id")
        every { getProjectByIdUseCase.getProjectById("bad-id", UserRole.ADMIN) } returns Result.failure(Exception("Project not found"))

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[31m❌ Project not found\u001B[0m")
        }
    }

    @Test
    fun `should show error message when updateProject fails with specific error`() {
        // Given
        val project = createProject()
        val id = project.id
        val errorMessage = "Update failed"
        every { consoleIO.read() } returnsMany listOf("3", id.toString(), "New name", "New desc")
        every { getProjectByIdUseCase.getProjectById(id.toString(), UserRole.ADMIN) } returns Result.success(project)
        every { updateProjectUseCase.updateProject(any(), UserRole.ADMIN) } returns Result.failure(Exception(errorMessage))

        // When
        projectScreen.execute()

        // Then
        verify {
            consoleIO.showWithLine("\u001B[31m❌ $errorMessage\u001B[0m")
        }
    }

    @Test
    fun `should add new project when selecting option 4`() {
        // Given
        every { consoleIO.read() } returnsMany listOf("4", "New project", "Some description")
        every { addProjectUseCase.addProject(any(), UserRole.ADMIN) } returns Result.success(true)

        // When
        projectScreen.execute()

        // Then
        verify {
            addProjectUseCase.addProject(match {
                it.name == "New project" &&
                        it.description == "Some description"
            }, UserRole.ADMIN)
        }

        verify {
            consoleIO.showWithLine(match { it.contains("✅ Project added successfully") })
        }
    }

    @Test
    fun `should show error when addProject fails`() {
        every { consoleIO.read() } returnsMany listOf("4", "name", "desc", "user1")
        every { addProjectUseCase.addProject(any(), UserRole.ADMIN) } returns Result.failure(Exception("Add failed"))

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[31m❌ Add failed\u001B[0m")
        }
    }


    @Test
    fun `should delete project when selecting option 5 and id exists`() {
        // Given
        val project = createProject(name = "To be deleted")
        val id = project.id
        every { consoleIO.read() } returnsMany listOf("5", id.toString())
        every { getProjectByIdUseCase.getProjectById(id.toString(), UserRole.ADMIN) } returns Result.success(project)
        every { deleteProjectUseCase.deleteProject(id.toString(), UserRole.ADMIN) } returns Result.success(true)

        // When
        projectScreen.execute()

        // Then
        verify {
            deleteProjectUseCase.deleteProject(id.toString(), UserRole.ADMIN)
        }

        verify {
            consoleIO.showWithLine(match { it.contains("\u001B[32m✅ Project deleted successfully.\u001B[0m") })
        }
    }

    @Test
    fun `should show error when deleteProject fails`() {
        val id = Uuid.random().toString()
        every { consoleIO.read() } returnsMany listOf("5", id)
        every { deleteProjectUseCase.deleteProject(id, UserRole.ADMIN) } returns Result.failure(Exception("Delete failed"))

        projectScreen.execute()

        verify {
            consoleIO.showWithLine("\u001B[31m❌ Delete failed\u001B[0m")
        }
    }



}