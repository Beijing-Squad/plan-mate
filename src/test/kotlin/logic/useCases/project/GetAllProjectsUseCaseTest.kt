package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.DataReadException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GetAllProjectsUseCaseTest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `should return projects when there is projects in repository`() {
        runTest {
            //Given
            val projects = listOf(createProject(), createProject())
            coEvery { projectRepository.getAllProjects() } returns projects
            // When
            val result = getAllProjectsUseCase.getAllProjects()
            // Then
            assertThat(result).containsExactlyElementsIn(projects)
        }
    }

    @Test
    fun `should return empty list when there is no projects in repository`() {
        runTest {
            //Given
            coEvery { projectRepository.getAllProjects() } returns emptyList()
            // When
            val result = getAllProjectsUseCase.getAllProjects()
            // Then
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun `should throw exception when there is error in csv file`() {
        runTest {
            // Given
            coEvery { projectRepository.getAllProjects() } throws DataReadException("")

            // When && Then
            assertThrows<DataReadException> { getAllProjectsUseCase.getAllProjects() }
        }
    }
}