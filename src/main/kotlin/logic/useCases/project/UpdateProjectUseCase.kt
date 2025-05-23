package logic.useCases.project

import logic.entities.Project
import logic.repository.ProjectsRepository

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    fun updateProject(newProject: Project) = projectsRepository.updateProject(newProject)
}
