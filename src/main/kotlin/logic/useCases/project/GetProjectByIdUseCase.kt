package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.CsvReadException
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectNotFoundException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository
import kotlin.uuid.ExperimentalUuidApi

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun getProjectById(projectId: String, role: UserRole): Result<Project> {
        return try {
            if (role != UserRole.ADMIN) {
                return Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
            }
            projectsRepository.getAllProjects()
                .find { it.id.toString() == projectId }
                ?.let { Result.success(it)
                } ?: Result.failure(ProjectNotFoundException("Project with ID:$projectId is not exist"))

        } catch (e: CsvReadException) {
            Result.failure(e)
        }
    }
}