package logic.repository

import logic.entities.Project

interface ProjectsRepository {
    fun getAllProjects(): List<Project>

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(newProjects: Project)

    fun getProjectById(projectId: String): Project
}