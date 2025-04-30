package logic.useCases.project


import fake.createProject
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import com.google.common.truth.Truth.assertThat
import logic.entities.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi


class UpdateProjectUseCaseTest {

    private lateinit var updateProjectUseCase: UpdateProjectUseCase
    private lateinit var projectRepository: ProjectsRepository


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        updateProjectUseCase = UpdateProjectUseCase(projectRepository)
    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should update project when project is exist`() {
        // Given
        val project=createProject()
        val projects = listOf(project,project)
        val newProject=project.copy(name = "New Project")
        every { projectRepository.getAllProjects() } returns projects

        // When
        val result=updateProjectUseCase.updateProject(newProject, UserRole.ADMIN).getOrThrow()

        //Then
        assertThat(result).isEqualTo(true)

    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when project is not exist`() {
        // Given
        val projects = listOf(createProject(),createProject())
        val newProject=createProject().copy(name = "New Project")
        every { projectRepository.getAllProjects() } returns projects

        // When && Then
        assertThrows<ProjectNotFoundException> {
            updateProjectUseCase.updateProject(newProject, UserRole.ADMIN).getOrThrow()
        }

    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when user is not admin`() {
        // Given
        val project = createProject()
        val projects = listOf(project, project)
        val newProject = createProject().copy(name = "New Project")
        every { projectRepository.getAllProjects() } returns projects

        // When && Then
        assertThrows<ProjectUnauthorizedUserException> {
            updateProjectUseCase.updateProject(newProject, UserRole.MATE).getOrThrow()
        }


    }
}