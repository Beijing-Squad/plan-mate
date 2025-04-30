package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.Assertions.*
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
        val project = createProject()
        val projectId = project.id.toString()
        every { projectRepository.getProjectById(projectId) } returns project
        // When
        val result = getProjectByIdUseCase.getProjectById(projectId, UserRole.ADMIN).getOrThrow()
        // Then
        assertThat(result.id).isEqualTo(project.id)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is notExist`() {
        // Given
        val projectId = createProject().id.toString()
        every { projectRepository.getProjectById(projectId) } throws ProjectNotFoundException("")

        // When && Then
        assertThrows<ProjectNotFoundException> { getProjectByIdUseCase.getProjectById(projectId, UserRole.ADMIN).getOrThrow() }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val project = createProject()
        val projectId = project.id.toString()
        every { projectRepository.getProjectById(projectId) } returns project

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> {
            getProjectByIdUseCase.getProjectById(projectId, UserRole.MATE).getOrThrow()
        }
    }


}