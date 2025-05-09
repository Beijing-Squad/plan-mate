package data.repository

import data.repository.mapper.toProjectDto
import data.repository.mapper.toProjectEntity
import data.repository.remoteDataSource.MongoDBDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val mongoDBDataSource: MongoDBDataSource
) : ProjectsRepository {

    override suspend fun getAllProjects(): List<Project> =
        mongoDBDataSource.getAllProjects().map { toProjectEntity(it) }

    override suspend fun addProject(project: Project) =
        mongoDBDataSource.addProject(toProjectDto(project))

    override suspend fun deleteProject(projectId: String) =
        mongoDBDataSource.deleteProject(projectId)

    override suspend fun updateProject(newProjects: Project) =
        mongoDBDataSource.updateProject(toProjectDto(newProjects))

    override suspend fun getProjectById(projectId: String): Project =
        toProjectEntity(mongoDBDataSource.getProjectById(projectId))
}