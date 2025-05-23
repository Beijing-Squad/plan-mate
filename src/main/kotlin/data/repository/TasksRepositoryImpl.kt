package data.repository

import data.repository.dataSource.TasksDataSource
import logic.entities.Task
import logic.repository.TasksRepository
class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
) : TasksRepository {

    override  fun getAllTasks(): List<Task> {
        return tasksDataSource.getAllTasks()
    }

    override  fun getTaskById(taskId: String): Task {
        return tasksDataSource.getTaskById(taskId)

    }

    override  fun addTask(task: Task) = tasksDataSource.addTask(task)

    override  fun deleteTask(taskId: String) = tasksDataSource.deleteTask(taskId)

    override  fun updateTask(updatedTask: Task): Task {
        return tasksDataSource.updateTask(updatedTask)

    }
}
