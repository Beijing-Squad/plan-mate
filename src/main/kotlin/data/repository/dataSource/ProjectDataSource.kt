package data.repository.dataSource

import logic.entities.Project

interface ProjectDataSource {
    fun getAllProjects(): List<Project>

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(newProjects: Project)

    fun getProjectById(projectId: String): Project
}

