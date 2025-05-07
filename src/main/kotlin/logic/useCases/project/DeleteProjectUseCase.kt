package logic.useCases.project

import logic.repository.ProjectsRepository

class DeleteProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun deleteProject(projectId: String) = projectsRepository.deleteProject(projectId)
}