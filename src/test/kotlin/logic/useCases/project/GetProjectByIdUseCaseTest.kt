package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class GetProjectByIdUseCaseTest {

    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return project when is exist`() {
        // Given
        val foundProject = createProject()
        val allProjects = listOf(foundProject, createProject())
        val foundProjectID = foundProject.id.toString()
        every { projectRepository.getAllProjects() } returns allProjects

        // When
        val result = getProjectByIdUseCase.getProjectById(foundProjectID, UserRole.ADMIN).getOrThrow()

        // Then
        assertThat(result).isEqualTo(foundProject)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is notExist`() {
        // Given
        val projectId = createProject().id.toString()
        val allProjects = listOf(createProject(), createProject())
        every { projectRepository.getAllProjects() } returns allProjects

        // When && Then
        assertThrows<ProjectNotFoundException> {
            getProjectByIdUseCase.getProjectById(projectId, UserRole.ADMIN).getOrThrow()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val projectId = createProject().id.toString()

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> {
            getProjectByIdUseCase.getProjectById(projectId, UserRole.MATE).getOrThrow()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when there is error in csv file`() {
        // Given
        val projectId = createProject().id.toString()
        every { projectRepository.getAllProjects() } throws CsvReadException("")

        // When && Then
        assertThrows<CsvReadException> {
            getProjectByIdUseCase.getProjectById(projectId, UserRole.ADMIN).getOrThrow()
        }
    }

}