package data.repository

import data.repository.dataSource.ProjectDataSource
import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectDataSource: ProjectDataSource
): ProjectsRepository{
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: String): Project {
        TODO("Not yet implemented")
    }

    override fun addProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override fun updateProject(projectId: String): Project {
        TODO("Not yet implemented")
    }

}