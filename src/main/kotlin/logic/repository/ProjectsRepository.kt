package logic.repository

import logic.entities.Project

interface ProjectsRepository{
    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addTask(project: Project)

    fun deleteTask(projectId: String)

    fun updateTask(projectId: String): Project

}