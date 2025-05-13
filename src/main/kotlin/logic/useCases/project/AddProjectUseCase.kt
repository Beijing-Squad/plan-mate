package logic.useCases.project

import logic.entity.Project
import logic.repository.ProjectsRepository


class AddProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend fun addProject(project: Project) = projectsRepository.addProject(project)
}