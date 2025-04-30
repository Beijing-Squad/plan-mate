package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class DeleteProjectUseCaseTest {

    private lateinit var deleteProject: DeleteProjectUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        deleteProject = DeleteProjectUseCase(projectRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project when project is exist`() {
        // Given
        val projectId = createProject().id.toString()
        // When
        val result = deleteProject.deleteProject(projectId, UserRole.ADMIN).getOrThrow()
        // Then
        assertThat(result).isEqualTo(true)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is notExist`() {
        // Given
        val projectId = createProject().id.toString()
        every { projectRepository.deleteProject(projectId) } throws ProjectNotFoundException("")
        // When && Then
        assertThrows<ProjectNotFoundException> { deleteProject.deleteProject(projectId, UserRole.ADMIN).getOrThrow() }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val projectId = createProject().id.toString()

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> { deleteProject.deleteProject(projectId, UserRole.MATE).getOrThrow() }

    }


}