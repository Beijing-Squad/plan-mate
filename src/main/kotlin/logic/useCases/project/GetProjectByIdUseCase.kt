package logic.useCases.project

import logic.entities.Project
import logic.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository,
) {
   fun getProjectById(projectId: String):Project = projectsRepository.getProjectById(projectId)
}