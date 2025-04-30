package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun getAllProjects(role: UserRole): Result<List<Project>> {
        return if (role != UserRole.ADMIN) {
            Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
        } else {
            Result.success(projectsRepository.getAllProjects())
        }
    }
}