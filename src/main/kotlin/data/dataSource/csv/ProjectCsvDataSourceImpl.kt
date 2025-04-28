package data.dataSource.csv

import data.dataSource.csv.utils.CsvPlanMateParser
import data.repository.dataSourceAbstraction.ProjectDataSource
import logic.entities.Project

class ProjectCsvDataSourceImpl (
    private val csvPlanMate: CsvPlanMateParser
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
