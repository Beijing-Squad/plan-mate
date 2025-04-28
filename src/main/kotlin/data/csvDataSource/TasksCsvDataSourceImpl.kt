package data.csvDataSource

import data.parser.CsvPlanMateParser
import data.parser.CsvPlanMateReader
import data.repository.dataSource.TasksDataSource
import logic.entities.Task

class TasksCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
): TasksDataSource {

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun updateTask(taskId: String): Task {
        TODO("Not yet implemented")
    }

}