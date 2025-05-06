package logic.useCases.project

import io.mockk.verify
import fake.createProject
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import logic.entities.exceptions.CsvWriteException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddProjectUseCaseTest {

    private lateinit var addProject: AddProjectUseCase
    private lateinit var projectRepository: ProjectsRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        addProject = AddProjectUseCase(projectRepository)
    }

    @Test
    fun `should add project successfully`() {
        // Given
        val project = createProject()
        every { projectRepository.addProject(project) } just Runs

        // When
        addProject.addProject(project)

        // Then
        verify { projectRepository.addProject(project) }}


    @Test
    fun `should throw CsvWriteException when repository throws`() {
        // Given
        val project = createProject()
        every { projectRepository.addProject(project) } throws CsvWriteException("Failed to write CSV")

        // When && Then
        assertThrows<CsvWriteException> {
            addProject.addProject(project)
        }
    }
}
