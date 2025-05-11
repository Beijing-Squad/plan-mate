package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateProjectUseCaseTest {

    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var projectRepository: ProjectsRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        updateProjectUseCase = UpdateProjectUseCase(projectRepository)
    }

    @Test
    fun `should update project successfully`() {
        runTest {
            // Given
            val updatedProject = createProject(name = "Updated Project", description = "Updated Description")

            coEvery { projectRepository.updateProject(updatedProject) } just Runs

            // When
            val result = updateProjectUseCase.updateProject(updatedProject)

            // Then
            assertThat(result).isNotNull()
        }
    }


    @Test
    fun `should throw ProjectNotFoundException when project does not exist`() {
        runTest {
            // Given
            val nonExistentProject = createProject(name = "Non Existent Project")

            coEvery { projectRepository.updateProject(nonExistentProject) } throws ProjectNotFoundException("Project not found")

            // When & Then
            val exception = assertThrows<ProjectNotFoundException> {
                updateProjectUseCase.updateProject(nonExistentProject)
            }

            assertThat(exception).hasMessageThat().contains("Project not found")
        }
    }

    @Test
    fun `should handle exceptions thrown by repository`() {
        runTest {
            // Given
            val invalidProject = createProject(name = "Invalid Project", description = "Invalid Description")

            coEvery { projectRepository.updateProject(invalidProject) } throws RuntimeException("Repository failure")

            // When & Then
            val exception = assertThrows<RuntimeException> {
                updateProjectUseCase.updateProject(invalidProject)
            }

            assertThat(exception).hasMessageThat().contains("Repository failure")
        }
    }
}
