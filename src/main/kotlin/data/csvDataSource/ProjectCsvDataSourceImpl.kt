package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.ProjectDataSource
import logic.entities.Project


class ProjectCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Project>
) : ProjectDataSource {

    override fun getAllProjects(): List<Project> = csvDataSource.loadAllDataFromFile()

    override fun addProject(project: Project) = csvDataSource.appendToFile(project)

    override fun deleteProject(projectId: String) = csvDataSource.deleteById(projectId)

    override fun updateProject(newProjects: List<Project>) = csvDataSource.updateFile(newProjects)

    override fun getProjectById(projectId: String): Project = csvDataSource.getById(projectId)
}
