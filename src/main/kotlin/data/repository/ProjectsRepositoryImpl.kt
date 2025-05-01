package data.repository

import data.repository.dataSource.ProjectDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
): ProjectsRepository{
    override fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override fun getProjectById(projectId: String): Project {
        return projectDataSource.getProjectById(projectId)
    }

    override fun addProject(project: Project) {
        return projectDataSource.addProject(project)
    }

    override fun deleteProject(projectId: String) {
        return  projectDataSource.deleteProject(projectId)
    }

    override fun updateProject(newProject: Project): Boolean {
        return projectDataSource.updateProject(newProject)
    }

}