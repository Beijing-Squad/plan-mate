package logic.useCases.project

import fake.createProject
import io.mockk.*
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectNotFoundException
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
        every { projectRepository.getAllProjects()}

        // When
        deleteProject.deleteProject(desiredProject.id.toString())
        // Then
        verify { projectRepository.deleteProject(desiredProject.id.toString()) }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is notExist`() {
        // Given
        val projectId = createProject().id.toString()
        every { projectRepository.deleteProject(projectId) } throws ProjectNotFoundException("")
        // When && Then
        assertThrows<ProjectNotFoundException> { deleteProject.deleteProject(projectId) }
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
        assertThrows<CsvWriteException> { deleteProject.deleteProject(desiredProject.id.toString()) }
    }
}