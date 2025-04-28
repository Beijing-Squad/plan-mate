package data.repository

import logic.entities.Project
import logic.repository.ProjectsRepository

class ProjectsRepositoryImpl(): ProjectsRepository{
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: String): Project {
        TODO("Not yet implemented")
    }

    override fun addTask(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(projectId: String) {
        TODO("Not yet implemented")
    }

    override fun updateTask(projectId: String): Project {
        TODO("Not yet implemented")
    }

}