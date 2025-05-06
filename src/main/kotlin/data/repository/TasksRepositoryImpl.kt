package data.repository

import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.repository.TasksRepository
class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
) : TasksRepository {

    override suspend fun getAllTasks(): List<Task> {
        return tasksDataSource.getAllTasks()
    }

    override suspend fun getTaskById(taskId: String): Task {
        return tasksDataSource.getTaskById(taskId)

    }

    override suspend fun addTask(task: Task) = tasksDataSource.addTask(task)

    override suspend fun deleteTask(taskId: String) = tasksDataSource.deleteTask(taskId)

    override suspend fun updateTask(updatedTask: Task): Task {
        return tasksDataSource.updateTask(updatedTask)

    }
}
