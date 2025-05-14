package logic.useCases.project

import logic.entity.Project
import logic.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository,
) {
   suspend fun getProjectById(projectId: String):Project = projectsRepository.getProjectById(projectId)
}