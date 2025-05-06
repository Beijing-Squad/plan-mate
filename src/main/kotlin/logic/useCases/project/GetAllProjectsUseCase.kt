package logic.useCases.project

import logic.entities.Project
import logic.repository.ProjectsRepository

class GetAllProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    fun getAllProjects(): List<Project> = projectsRepository.getAllProjects()
}