package data.repository.remoteDataSource

import logic.entities.Project

interface ProjectMongoDataSource {
    suspend fun getAllProjects(): List<Project>
    suspend fun addProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(newProject: Project)
    suspend fun getProjectById(projectId: String): Project
}