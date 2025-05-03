package data.repository

import com.google.common.truth.Truth.assertThat
import data.repository.dataSource.ProjectDataSource
import fake.createProject
import io.mockk.*
import logic.entities.exceptions.CsvWriteException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectsRepositoryImplTest {

    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var projectsRepository: ProjectsRepositoryImpl


    @BeforeEach
    fun setup() {
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
    fun `should update a project by ID when it exists`() {
        // Given
        val newProjects = listOf(createProject(), createProject())
        every { projectDataSource.updateProject(newProjects) } just Runs

        // When
        projectsRepository.updateProject(newProjects)

        //Then
        verify {
            projectDataSource.updateProject(newProjects)
        }
    }

    @Test
    fun `should throw exception when updating project that does not exist`() {
        // Given
        val newProjects = listOf(createProject(), createProject())
        every {
            projectDataSource.updateProject(newProjects)
        } throws CsvWriteException("Error saving to CSV file")

        // When & Then
        assertThrows<CsvWriteException> {
            projectsRepository.updateProject(newProjects)
        }
    }

}