package data.dataSource.csv

import data.dataSource.csv.utils.CsvPlanMateParser
import data.dataSource.csv.utils.CsvPlanMateReader
import data.repository.dataSourceAbstraction.ProjectDataSource
import logic.entities.Project

class ProjectCsvDataSourceImpl (
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
): ProjectDataSource{

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
