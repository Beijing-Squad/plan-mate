package ui.service

import logic.entities.Project

interface ProjectUIService {
    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(projectId: String): Project
}