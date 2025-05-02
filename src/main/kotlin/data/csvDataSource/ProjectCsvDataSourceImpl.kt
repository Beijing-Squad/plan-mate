package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.ProjectDataSource
import logic.entities.Project
import logic.entities.Task

class ProjectCsvDataSourceImpl (
    private val csvDataSource: CsvDataSourceImpl<Project>
): ProjectDataSource {

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

    override fun getTaskByProjectId(projectId: String): List<Task> {
        TODO("Not yet implemented")
    }

}
