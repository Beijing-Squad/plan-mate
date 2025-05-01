package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class GetAllProjectsUseCaseTest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }
    @Test
    fun `should return projects when there is projects in repository`(){
        //Given
        val projects=listOf(createProject(),createProject())
        every { projectRepository.getAllProjects() } returns projects
        // When
        val result = getAllProjectsUseCase.getAllProjects(UserRole.ADMIN)
        // Then
        assertThat(result.getOrThrow()).containsExactlyElementsIn(projects)
    }

    @Test
    fun `should return empty list when there is no projects in repository`(){
        //Given
        every { projectRepository.getAllProjects() } returns listOf<Project>()
        // When
        val result = getAllProjectsUseCase.getAllProjects(UserRole.ADMIN)
        // Then
        assertThat(result.getOrThrow()).containsExactlyElementsIn(listOf<Project>())
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        //Given
        val projects=listOf(createProject(),createProject())
        every { projectRepository.getAllProjects() } returns projects

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> {
            getAllProjectsUseCase.getAllProjects( UserRole.MATE).getOrThrow()
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when there is error in csv file`() {
        // Given
        every { projectRepository.getAllProjects() } throws CsvReadException("")

        // When && Then
        assertThrows<CsvReadException> { getAllProjectsUseCase.getAllProjects(UserRole.ADMIN).getOrThrow() }

    }

}