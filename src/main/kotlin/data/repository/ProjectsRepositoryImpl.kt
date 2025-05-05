package data.repository

import data.repository.dataSource.ProjectDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectsRepository {
    override fun getAllProjects(): List<Project> = projectDataSource.getAllProjects()

    override fun addProject(project: Project) = projectDataSource.addProject(project)

    override fun deleteProject(projectId: String) = projectDataSource.deleteProject(projectId)

    override fun updateProject(newProjects: List<Project>) = projectDataSource.updateProject(newProjects)

    override fun getProjectById(projectId: String): Project = projectDataSource.getProjectById(projectId)
}