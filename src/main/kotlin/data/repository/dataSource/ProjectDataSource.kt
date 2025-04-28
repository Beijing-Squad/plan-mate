package data.repository.dataSource

import logic.entities.Project

interface ProjectDataSource{

    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addProject(project: Project)

    fun deleteProject(projectId: String)

    fun updateProject(projectId: String): Project

}

