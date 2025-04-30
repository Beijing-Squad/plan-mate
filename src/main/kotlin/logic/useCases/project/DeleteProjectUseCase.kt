package logic.useCases.project

import logic.entities.UserRole
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun deleteProject(projectId: String,role: UserRole):Result<Boolean> {
        return try {
            if(role != UserRole.ADMIN)
                Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
            else{
                projectsRepository.deleteProject(projectId)
                Result.success(true)
            }

        }catch (e:Exception){
            Result.failure(ProjectNotFoundException("Project with ID:$projectId is not exist"))
        }
    }
}