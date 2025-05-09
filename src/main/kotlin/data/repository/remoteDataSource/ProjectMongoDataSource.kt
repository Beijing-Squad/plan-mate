package data.repository.remoteDataSource

import data.dto.ProjectDTO

interface ProjectMongoDataSource {
    suspend fun getAllProjects(): List<ProjectDTO>
    suspend fun addProject(project: ProjectDTO)
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(newProject: ProjectDTO)
    suspend fun getProjectById(projectId: String): ProjectDTO
}