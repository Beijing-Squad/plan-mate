package data.local.csvDataSource

import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.TaskNotFoundException
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TasksCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Task>
) : TasksDataSource {

    override suspend  fun getAllTasks(): List<Task> {
        return csvDataSource.loadAllDataFromFile()
    }

    override suspend  fun getTaskById(taskId: String): Task {

        val tasks = csvDataSource.loadAllDataFromFile()
        return tasks.find { it.id.toString() == taskId }
            ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    override suspend  fun addTask(task: Task) = csvDataSource.appendToFile(task)


    override suspend  fun deleteTask(taskId: String)  = csvDataSource.deleteById(taskId)

    override suspend  fun updateTask(updatedTask: Task): Task {

        val tasks = csvDataSource.loadAllDataFromFile().toMutableList()

        val taskIndex = tasks.indexOfFirst { it.id.toString() == updatedTask.id.toString() }
        if (updatedTask.title.isEmpty()) throw InvalidInputException("Task title cannot be empty")
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID ${updatedTask.id} not found")

        tasks[taskIndex] = updatedTask
        csvDataSource.updateFile(tasks)
        return updatedTask
    }
}
