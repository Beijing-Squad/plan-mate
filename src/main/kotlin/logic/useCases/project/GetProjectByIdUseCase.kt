package logic.useCases.project

import logic.repository.ProjectsRepository

class GetProjectByIdUseCase(
    private val projectsRepository: ProjectsRepository
) {
}