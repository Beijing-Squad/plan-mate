package data.dataSource.csv

import data.repository.dataSourceAbstraction.ProjectDataSource
import logic.entities.Project
import java.io.File

class ProjectCsvDataSource (
    private val file: File
): ProjectDataSource{

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
