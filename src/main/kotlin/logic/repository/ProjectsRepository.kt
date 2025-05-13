package logic.repository

import logic.entity.Project

interface ProjectsRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun addProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(newProjects: Project)
    suspend fun getProjectById(projectId: String): Project
}