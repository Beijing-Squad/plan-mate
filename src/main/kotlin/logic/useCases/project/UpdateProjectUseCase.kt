package logic.useCases.project

import logic.entity.Project
import logic.repository.ProjectsRepository

class UpdateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend fun updateProject(newProject: Project) = projectsRepository.updateProject(newProject)
}
