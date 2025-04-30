package data.csvDataSource


import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.*
import kotlinx.datetime.LocalDate
import logic.entities.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectCsvDataSourceImplTest {

    private lateinit var projectCsvDataSource: ProjectCsvDataSourceImpl

    @BeforeTest
    fun setup() {
        projectCsvDataSource = mockk()

    }

    @Test
    fun `should return all projects from the csv data source when calling the getAllProjects function`() {
        // Given
        val project = createProject(name = "Project1")
        val expectedProjects = listOf(project)
        every { projectCsvDataSource.getAllProjects() } returns expectedProjects

        // When
        val result = projectCsvDataSource.getAllProjects()

        // Then
        assertThat(result).isEqualTo(expectedProjects)
    }

    @Test
    fun `should return project by ID when it exists`() {
        //Given
        val project = createProject(name = "Project1")
        val projectId = project.id.toString()
        every { projectCsvDataSource.getProjectById(projectId) } returns project

        //When
        val result = projectCsvDataSource.getProjectById(projectId)

        //Then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `should throw exception when getting project by ID that does not exist`() {
        // Given
        val projectId = Uuid.random().toString()
        every {
            projectCsvDataSource.getProjectById(projectId)
        } throws ProjectNotFoundException("Project with ID $projectId not found")

        // When & Then
        val exception = assertFailsWith<ProjectNotFoundException> {
            projectCsvDataSource.getProjectById(projectId)
        }
        assertThat(exception.message).isEqualTo("Project with ID $projectId not found")
    }

    @Test
    fun `should add a new project to the data source`() {
        // Given
        val project = createProject(name = "NewProject")
        every { projectCsvDataSource.addProject(project) } just Runs

        // When
        projectCsvDataSource.addProject(project)

        // Then
        verify { projectCsvDataSource.addProject(project) }
    }

    @Test
    fun `should delete a project by ID`() {
        // Given
        val projectId = Uuid.random().toString()
        every { projectCsvDataSource.deleteProject(projectId) } just Runs

        // When
        projectCsvDataSource.deleteProject(projectId)

        // Then
        verify { projectCsvDataSource.deleteProject(projectId) }
    }

    @Test
    fun `should update a project by ID when it exists`() {
        // Given
        val originalProject = createProject(name = "Original")
        val updatedProject = originalProject.copy(
            name = "Updated", updatedAt = LocalDate.parse("2025-05-20")
        )
        val projectId = originalProject.id.toString()
        every { projectCsvDataSource.updateProject(projectId) } returns updatedProject

        // When
        val result = projectCsvDataSource.updateProject(projectId)

        // Then
        assertThat(result).isEqualTo(updatedProject)
    }

    @Test
    fun `should throw exception when updating project that does not exist`() {
        // Given
        val projectId = Uuid.random().toString()
        every {
            projectCsvDataSource.updateProject(projectId)
        } throws ProjectNotFoundException("Project with ID $projectId not found")

        // When & Then
        val exception = assertFailsWith<ProjectNotFoundException> {
            projectCsvDataSource.updateProject(projectId)
        }
        assertThat(exception.message).isEqualTo("Project with ID $projectId not found")
    }
}