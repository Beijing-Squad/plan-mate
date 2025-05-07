package data.csvDataSource


import com.google.common.truth.Truth.assertThat
import data.local.csvDataSource.ProjectCsvDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import fake.createProject
import io.mockk.*
import logic.entities.Project
import logic.entities.exceptions.CsvWriteException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectCsvDataSourceImplTest {

    private lateinit var projectCsvDataSource: ProjectCsvDataSourceImpl
    private lateinit var csvDataSource: CsvDataSourceImpl<Project>

    @BeforeTest
    fun setup() {
        csvDataSource= mockk(relaxed = true)
        projectCsvDataSource = ProjectCsvDataSourceImpl(csvDataSource)

    }

    @Test
    fun `should return all projects from the csv data source when calling the getAllProjects function`() {
        // Given
        val project = createProject(name = "Project1")
        val expectedProjects = listOf(project)
        every { csvDataSource.loadAllDataFromFile() } returns expectedProjects

        // When
        val result = projectCsvDataSource.getAllProjects()

        // Then
        assertThat(result).isEqualTo(expectedProjects)
    }


    @Test
    fun `should add a new project to the data source when project is valid`() {
        // Given
        val project = createProject(name = "NewProject")
        every { csvDataSource.appendToFile(project) } just Runs

        // When
        projectCsvDataSource.addProject(project)

        // Then
        verify { csvDataSource.appendToFile(project) }
    }

    @Test
    fun `should delete a project by ID`() {
        // Given
        val projectId = Uuid.random().toString()
        every { csvDataSource.deleteById(projectId) } just Runs

        // When
        projectCsvDataSource.deleteProject(projectId)

        // Then
        verify { csvDataSource.deleteById(projectId) }
    }

    @Test
    fun `should update a project by ID when it exists`() {
        // Given
        val newProjects=createProject()

        // When
        projectCsvDataSource.updateProject(newProjects)

        // Then
        verify {
            csvDataSource.updateItem(newProjects)
        }
    }

    @Test
    fun `should throw exception when updating project that does not exist`() {
        // Given
        val newProjects = createProject()
        every {
            csvDataSource.updateItem (newProjects)
        } throws CsvWriteException("Error saving to CSV file")

        // When & Then
        assertThrows<CsvWriteException> {
            projectCsvDataSource.updateProject(newProjects)
        }
    }
}