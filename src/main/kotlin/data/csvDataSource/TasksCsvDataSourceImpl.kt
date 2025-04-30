package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.TasksDataSource
import logic.entities.Task

class TasksCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Task>
): TasksDataSource {

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override fun addTask(task: Task) = csvDataSource.appendToFile(task)


    override fun deleteTask(taskId: String)  = csvDataSource.deleteById(taskId)

    override fun updateTask(taskId: String): Task {
        TODO("Not yet implemented")
    }

}