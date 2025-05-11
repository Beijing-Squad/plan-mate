package data.repository

import data.repository.mapper.toProjectDto
import data.repository.mapper.toProjectEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : ProjectsRepository {

    override suspend fun getAllProjects(): List<Project> =
        remoteDataSource.getAllProjects().map { it.toProjectEntity() }

    override suspend fun addProject(project: Project) =
        remoteDataSource.addProject(project.toProjectDto())

    override suspend fun deleteProject(projectId: String) =
        remoteDataSource.deleteProject(projectId)

    override suspend fun updateProject(newProjects: Project) =
        remoteDataSource.updateProject(newProjects.toProjectDto())

    override suspend fun getProjectById(projectId: String): Project =
        remoteDataSource.getProjectById(projectId).toProjectEntity()
}