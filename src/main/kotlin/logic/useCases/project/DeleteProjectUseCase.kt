package logic.useCases.project

import logic.entities.UserRole
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import kotlin.uuid.ExperimentalUuidApi

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun deleteProject(projectId: String,role: UserRole):Result<Boolean> {
        return try {
            if(role != UserRole.ADMIN) {
                Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
            }
            else if(!isProjectExists(projectId)){
                Result.failure(ProjectNotFoundException("There is no Project with ID:${projectId}"))
            }
            else{

                projectsRepository.deleteProject(projectId)
                Result.success(true)
            }

        }catch (e:CsvWriteException){
            Result.failure(e)
        }
    }
    @OptIn(ExperimentalUuidApi::class)
    private fun isProjectExists(projectId: String): Boolean = projectsRepository.getAllProjects().any { it.id.toString() == projectId }


}