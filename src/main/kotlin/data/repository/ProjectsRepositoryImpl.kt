package data.repository

import data.repository.mapper.toProjectDto
import data.repository.mapper.toProjectEntity
import data.repository.remoteDataSource.ProjectMongoDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectMongoDataSource: ProjectMongoDataSource
) : ProjectsRepository {

    override suspend fun getAllProjects(): List<Project> =
        projectMongoDataSource.getAllProjects().map { toProjectEntity(it) }

    override suspend fun addProject(project: Project) =
        projectMongoDataSource.addProject(toProjectDto(project))

    override suspend fun deleteProject(projectId: String) =
        projectMongoDataSource.deleteProject(projectId)

    override suspend fun updateProject(newProjects: Project) =
        projectMongoDataSource.updateProject(toProjectDto(newProjects))

    override suspend fun getProjectById(projectId: String): Project =
        toProjectEntity(projectMongoDataSource.getProjectById(projectId))
}