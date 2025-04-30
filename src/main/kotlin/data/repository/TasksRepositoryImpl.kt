package data.repository

import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import logic.repository.TasksRepository
import kotlin.uuid.ExperimentalUuidApi
class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
) : TasksRepository {
    private val tasks = mutableListOf<Task>()

    override fun getAllTasks(): List<Task> {
        return tasks.toList()
    }

    override fun getTaskById(taskId: String): Task {
        return tasksDataSource.getTaskById(taskId)
    }

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateTask(taskId: String, updatedTask: Task): Task {
        val index = tasks.indexOfFirst { it.id.toString() == taskId }
        if (index == -1) throw TaskNotFoundException("Task with ID $taskId not found")

        tasks[index] = updatedTask
        return updatedTask
    }
}
