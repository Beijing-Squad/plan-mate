package logic.useCases.project

import fake.createProject
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.exceptions.DataWriteException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
class AddProjectUseCaseTest {

    private lateinit var addProject: AddProjectUseCase
    private lateinit var projectRepository: ProjectsRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        addProject = AddProjectUseCase(projectRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should add project successfully`() {
        runTest {
            // Given
            val project = createProject()
            coEvery { projectRepository.addProject(project) } just Runs

            // When
            addProject.addProject(project)

            // Then
            coVerify { projectRepository.addProject(project) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw CsvWriteException when repository throws`() {
        runTest {
            // Given
            val project = createProject()
            coEvery { projectRepository.addProject(project) } throws DataWriteException("Failed to write CSV")

            // When && Then
            assertThrows<DataWriteException> {
                addProject.addProject(project)
            }
        }
    }
}
