package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun getProjectById(projectId: String, role: UserRole): Result<Project> {
        return try {
            if (role != UserRole.ADMIN) {
                Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
            } else {
                Result.success(projectsRepository.getProjectById(projectId))
            }
        } catch (e: Exception) {
            return Result.failure(ProjectNotFoundException("Project with ID:$projectId is not exist"))
        }
    }
}