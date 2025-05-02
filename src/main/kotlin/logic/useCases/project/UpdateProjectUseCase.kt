package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.CsvWriteException
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

        } else {
            try {
                val newProjects = projectsRepository.getAllProjects().map { project ->
                    if (project.id == newProject.id) newProject else project
                }

                projectsRepository.updateProject(newProjects)
                Result.success(UPDATE_PROJECT_SUCCESSFULLY)
            } catch (e: CsvWriteException) {
                Result.failure(e)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun isProjectExists(newProject: Project): Boolean =
        projectsRepository.getAllProjects().any { it.id == newProject.id }

    companion object {
        private const val UPDATE_PROJECT_SUCCESSFULLY = true
    }
}