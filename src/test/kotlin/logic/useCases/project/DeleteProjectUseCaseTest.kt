package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.CsvWriteException
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
        val desiredProject=createProject()
        val allProjects=listOf(desiredProject,createProject())

        every { projectRepository.getAllProjects()} returns allProjects
        // When
        val result = deleteProject.deleteProject(desiredProject.id.toString(), UserRole.ADMIN).getOrThrow()
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
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when there is error in csv file`() {
        // Given
        val desiredProject=createProject()
        val allProjects=listOf(desiredProject,createProject())

        every { projectRepository.deleteProject(desiredProject.id.toString()) } throws CsvWriteException("")
        every { projectRepository.getAllProjects() } returns allProjects

        // When && Then
        assertThrows<CsvWriteException> { deleteProject.deleteProject(desiredProject.id.toString(),UserRole.ADMIN).getOrThrow() }

    }


}