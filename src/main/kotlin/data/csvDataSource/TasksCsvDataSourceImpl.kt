package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class TasksCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Task>
) : TasksDataSource {

    override fun getAllTasks(): List<Task> {
        return csvDataSource.loadAllDataFromFile()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getTaskById(taskId: String): Task {
        val tasks = csvDataSource.loadAllDataFromFile()
        return tasks.find { it.id.toString() == taskId }
            ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    override fun addTask(task: Task) = csvDataSource.appendToFile(task)


    override fun deleteTask(taskId: String)  = csvDataSource.deleteById(taskId)

    @OptIn(ExperimentalUuidApi::class)
    override fun updateTask(taskId: String, updatedTask: Task): Task {
        val tasks = csvDataSource.loadAllDataFromFile().toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id.toString() == taskId }
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID $taskId not found")

        tasks[taskIndex] = updatedTask
        csvDataSource.updateFile(tasks)
        return updatedTask
    }
}
