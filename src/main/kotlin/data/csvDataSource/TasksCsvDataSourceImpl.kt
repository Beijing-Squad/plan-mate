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

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateTask(updatedTask: Task): Task {
        val tasks = csvDataSource.loadAllDataFromFile().toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id.toString() == updatedTask.id.toString() }
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID ${updatedTask.id} not found")

        tasks[taskIndex] = updatedTask
        csvDataSource.updateFile(tasks)
        return updatedTask
    }

}
