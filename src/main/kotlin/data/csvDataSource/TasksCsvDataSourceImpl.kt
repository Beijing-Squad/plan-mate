package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class TasksCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Task>
) : TasksDataSource {
    private val tasks = csvDataSource.loadAllDataFromFile().toMutableList()

    override fun getAllTasks(): List<Task> {
        return tasks.toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getTaskById(taskId: String): Task {

        return tasks.find { it.id.toString() == taskId }
            ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateTask(taskId: String, updatedTask: Task): Task {
        val taskIndex = tasks.indexOfFirst { it.id.toString() == taskId }
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID $taskId not found")

        tasks[taskIndex] = updatedTask
        csvDataSource.updateFile(tasks)
        return updatedTask
    }

}