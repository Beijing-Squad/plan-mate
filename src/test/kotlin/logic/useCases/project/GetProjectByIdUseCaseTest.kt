package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.exceptions.ProjectNotFoundException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetProjectByIdUseCaseTest {

    private val projectRepository = mockk<ProjectsRepository>()
    private val getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return project by id successfully`() {
        runTest {
            // Given
            val projectId = Uuid.random()
            val expectedProject = createProject().copy(id = projectId)

            coEvery { projectRepository.getProjectById(projectId.toString()) } returns expectedProject

            // When
            val result = getProjectByIdUseCase.getProjectById(projectId.toString())

            // Then
            assertThat(result).isEqualTo(expectedProject)
        }
    }

    @Test
    fun `should throw exception when project is not found`() {
        runTest {
            // Given
            val invalidProjectId = "invalid-id"
            coEvery { projectRepository.getProjectById(invalidProjectId) } throws ProjectNotFoundException("Not found")

            // When & Then
            try {
                getProjectByIdUseCase.getProjectById(invalidProjectId)
            } catch (e: ProjectNotFoundException) {
                assertThat(e.message).isEqualTo("Not found")
            }
        }
    }
}
