package logic.repository

import logic.entities.Project

interface ProjectsRepository{
    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(projectId: String): Project

}