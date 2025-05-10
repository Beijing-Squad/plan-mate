package logic.useCases.project

import fake.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.CsvWriteException
import logic.exceptions.ProjectNotFoundException
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
        runTest {
            // Given
            val desiredProject = createProject()
            coEvery { projectRepository.getAllProjects() }

            // When
            deleteProject.deleteProject(desiredProject.id.toString())
            // Then
            coVerify { projectRepository.deleteProject(desiredProject.id.toString()) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is notExist`() {
        runTest {
            // Given
            val projectId = createProject().id.toString()
            coEvery { projectRepository.deleteProject(projectId) } throws ProjectNotFoundException("")
            // When && Then
            assertThrows<ProjectNotFoundException> { deleteProject.deleteProject(projectId) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when there is error in csv file`() {
        runTest {
            // Given
            val desiredProject = createProject()
            val allProjects = listOf(desiredProject, createProject())

            coEvery { projectRepository.deleteProject(desiredProject.id.toString()) } throws CsvWriteException("")
            coEvery { projectRepository.getAllProjects() } returns allProjects

            // When && Then
            assertThrows<CsvWriteException> { deleteProject.deleteProject(desiredProject.id.toString()) }
        }
    }
}