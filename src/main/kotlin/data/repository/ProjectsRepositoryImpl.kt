package data.repository

import data.repository.remoteDataSource.ProjectMongoDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectMongoDataSource: ProjectMongoDataSource
) : ProjectsRepository {
    override suspend fun getAllProjects(): List<Project> = projectMongoDataSource.getAllProjects()
    override suspend fun addProject(project: Project) = projectMongoDataSource.addProject(project)
    override suspend fun deleteProject(projectId: String) = projectMongoDataSource.deleteProject(projectId)
    override suspend fun updateProject(newProjects: Project) = projectMongoDataSource.updateProject(newProjects)
    override suspend fun getProjectById(projectId: String): Project = projectMongoDataSource.getProjectById(projectId)
}