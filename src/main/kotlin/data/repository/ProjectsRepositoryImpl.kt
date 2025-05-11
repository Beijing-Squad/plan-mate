package data.repository

import data.repository.mapper.toDto
import data.repository.mapper.toEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : ProjectsRepository {

    override suspend fun getAllProjects(): List<Project> =
        remoteDataSource.getAllProjects().map { it.toEntity() }

    override suspend fun addProject(project: Project) =
        remoteDataSource.addProject(project.toDto())

    override suspend fun deleteProject(projectId: String) =
        remoteDataSource.deleteProject(projectId)

    override suspend fun updateProject(newProjects: Project) =
        remoteDataSource.updateProject(newProjects.toDto())

    override suspend fun getProjectById(projectId: String): Project =
        remoteDataSource.getProjectById(projectId).toEntity()
}