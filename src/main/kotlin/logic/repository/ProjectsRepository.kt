package logic.repository

import logic.entities.Project
import logic.entities.Task

interface ProjectsRepository{
    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(projectId: String): Project

    fun getTaskByProjectId(projectId: String): List<Task>

}