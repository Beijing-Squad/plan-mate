package logic.useCases.project

import logic.entity.Project
import logic.repository.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend fun getAllProjects(): List<Project> = projectsRepository.getAllProjects()
}