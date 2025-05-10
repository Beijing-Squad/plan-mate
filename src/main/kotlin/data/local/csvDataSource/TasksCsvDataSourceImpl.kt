package data.local.csvDataSource

import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.localDataSource.TasksDataSource
import logic.entities.Task
import logic.exceptions.InvalidInputException
import logic.exceptions.TaskNotFoundException
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TasksCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Task>
) : TasksDataSource {

    override   fun getAllTasks(): List<Task> {
        return csvDataSource.loadAllDataFromFile()
    }

    override   fun getTaskById(taskId: String): Task {

        val tasks = csvDataSource.loadAllDataFromFile()
        return tasks.find { it.id.toString() == taskId }
            ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    override   fun addTask(task: Task) = csvDataSource.appendToFile(task)


    override   fun deleteTask(taskId: String)  = csvDataSource.deleteById(taskId)

    override   fun updateTask(updatedTask: Task): Task {

        val tasks = csvDataSource.loadAllDataFromFile().toMutableList()

        val taskIndex = tasks.indexOfFirst { it.id.toString() == updatedTask.id.toString() }
        if (updatedTask.title.isEmpty()) throw InvalidInputException("Task title cannot be empty")
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID ${updatedTask.id} not found")

        tasks[taskIndex] = updatedTask
        csvDataSource.updateFile(tasks)
        return updatedTask
    }
}
