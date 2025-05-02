package logic.useCases.project

import com.google.common.truth.Truth.assertThat
import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectAlreadyExistsException
import logic.entities.exceptions.ProjectNameIsEmptyException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddProjectUseCaseTest {

    private lateinit var addProject: AddProjectUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        addProject = AddProjectUseCase(projectRepository)
    }

    @Test
    fun `should add new project when project is valid`() {
        // Given
        val project = createProject()
        // When
        val result = addProject.addProject(project,UserRole.ADMIN).getOrThrow()
        // Then
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should throw exception when project name is empty`() {
        // Given
        val project = createProject(name = "")
        // When && Then
        assertThrows<ProjectNameIsEmptyException> { addProject.addProject(project,UserRole.ADMIN).getOrThrow() }
    }

    @Test
    fun `should throw exception when project is duplicate`() {
        // Given
        val project = createProject()
        every { projectRepository.getAllProjects() } returns listOf(project)

        // When && Then
        assertThrows<ProjectAlreadyExistsException> { addProject.addProject(project,UserRole.ADMIN).getOrThrow() }

    }

    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val project = createProject()

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> { addProject.addProject(project,UserRole.MATE).getOrThrow() }

    }
    @Test
    fun `should throw exception when there is error in csv file`() {
        // Given
        val project = createProject()
        every { projectRepository.addProject(project) } throws CsvWriteException("")

        // When && Then
        assertThrows<CsvWriteException> { addProject.addProject(project,UserRole.ADMIN).getOrThrow() }

    }


}