package logic.useCases.project

import logic.entities.Project
import logic.entities.UserRole
import logic.entities.exceptions.CsvWriteException
import logic.entities.exceptions.ProjectAlreadyExistsException
import logic.entities.exceptions.ProjectNameIsEmptyException
import logic.entities.exceptions.ProjectUnauthorizedUserException
import logic.repository.ProjectsRepository


class AddProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {

    fun addProject(project: Project, role: UserRole): Result<Boolean> {

        return if (checkDuplicateProject(project))
            Result.failure(ProjectAlreadyExistsException("Project is already exists"))
        else if (project.name.isEmpty())
            Result.failure(ProjectNameIsEmptyException("Project Have No Name"))
        else if (role != UserRole.ADMIN)
            Result.failure(ProjectUnauthorizedUserException("User Not Authorized"))
        else {
            try {
                projectsRepository.addProject(project)
                Result.success(true)
            } catch (e: CsvWriteException) {
                Result.failure(e)
            }

        }
    }

    private fun checkDuplicateProject(project: Project) =
        projectsRepository.getAllProjects().any { it.name == project.name }
}