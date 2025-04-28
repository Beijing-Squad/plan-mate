package data.repository.dataSourceAbstraction

import logic.entities.Project

interface ProjectDataSource{

    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: String): Project

    fun addTask(project: Project)

    fun deleteTask(projectId: String)

    fun updateTask(projectId: String): Project

}

