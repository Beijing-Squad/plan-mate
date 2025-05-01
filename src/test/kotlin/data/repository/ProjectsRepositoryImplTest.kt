package data.repository

import com.google.common.truth.Truth.assertThat
import data.repository.dataSource.ProjectDataSource
import fake.createProject
import io.mockk.*
import kotlinx.datetime.LocalDate
import logic.entities.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectsRepositoryImplTest {

    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var projectsRepository: ProjectsRepositoryImpl

    @BeforeEach
    fun setup(){
        projectDataSource = mockk()
        projectsRepository = ProjectsRepositoryImpl(projectDataSource)
    }

    @Test
    fun `should return all projects from the project data source`() {
        //Given
        val project = createProject(name = "Project1")
        val expectedProjects = listOf(project)
        every { projectDataSource.getAllProjects() } returns expectedProjects

        //When
        val result = projectsRepository.getAllProjects()

        //Then
        assertThat(result).isEqualTo(expectedProjects)
    }


    @Test
    fun `should return project by ID when it exists`(){
        //Given
        val project = createProject(name = "Project Man")
        val projectId = project.id.toString()
        every { projectDataSource.getProjectById(projectId) } returns project

        //When
        val result = projectsRepository.getProjectById(projectId)

        //Then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `should throw exception when getting project by ID that does not exist`() {
        // Given
        val projectId = Uuid.random().toString()
        every {
            projectDataSource.getProjectById(projectId)
        } throws ProjectNotFoundException("Project with ID $projectId not found")

        // When & Then
        val exception = assertFailsWith<ProjectNotFoundException> {
            projectsRepository.getProjectById(projectId)
        }
        assertThat(exception.message).isEqualTo("Project with ID $projectId not found")
    }

    @Test
    fun `should add a new project to the data source`() {
        // Given
        val project = createProject(name = "New Project")
        every { projectDataSource.addProject(project) } just Runs

        // When
        projectsRepository.addProject(project)

        // Then
        verify { projectDataSource.addProject(project) }
    }

    @Test
    fun `should delete a project by ID`() {
        // Given
        val projectId = Uuid.random().toString()
        every { projectDataSource.deleteProject(projectId) } just Runs

        // When
        projectsRepository.deleteProject(projectId)

        // Then
        verify { projectDataSource.deleteProject(projectId) }
    }

    @Test
    fun `should return true when updating a project that exists`() {
        // Given
        val project = createProject(name = "Original")
        val updatedProject = project.copy(
            name = "Updated", updatedAt = LocalDate.parse("2025-05-20")
        )
        every { projectDataSource.updateProject(updatedProject) } returns true

        // When
        val result = projectsRepository.updateProject(updatedProject)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when updating a project that does not exist`() {
        // Given
        val project = createProject(name = "NonExistent")
        every { projectDataSource.updateProject(project) } returns false

        // When
        val result = projectsRepository.updateProject(project)

        // Then
        assertThat(result).isFalse()
    }

}