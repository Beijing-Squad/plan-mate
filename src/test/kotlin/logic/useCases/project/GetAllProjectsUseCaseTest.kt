package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.exceptions.CsvReadException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        val result = getAllProjectsUseCase.getAllProjects()
        // Then
        assertThat(result).containsExactlyElementsIn(projects)
    }

    @Test
    fun `should return empty list when there is no projects in repository`(){
        //Given
        every { projectRepository.getAllProjects() } returns emptyList()
        // When
        val result = getAllProjectsUseCase.getAllProjects()
        // Then
        assertThat(result).isEmpty()
    }
    @Test
    fun `should throw exception when there is error in csv file`() {
        // Given
        every { projectRepository.getAllProjects() } throws CsvReadException("")

        // When && Then
        assertThrows<CsvReadException> { getAllProjectsUseCase.getAllProjects() }
    }
}