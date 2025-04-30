package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateProject(newProject: Project, role: UserRole): Result<Boolean> {
        return if (role != UserRole.ADMIN) {
            Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
        } else if (!isProjectExists(newProject)) {
            Result.failure(ProjectNotFoundException("There is no Project with ID:${newProject.id}"))
        }else{
            projectsRepository.updateProject(newProject)
            Result.success(true)
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    private fun isProjectExists(newProject: Project): Boolean = projectsRepository.getAllProjects().any { it.id == newProject.id }
}