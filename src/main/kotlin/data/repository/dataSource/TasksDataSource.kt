package data.repository.dataSource

import logic.entities.Task

interface TasksDataSource {

    suspend fun getAllTasks(): List<Task>

    suspend fun getTaskById(taskId: String): Task

    suspend fun addTask(task: Task)

    suspend fun deleteTask(taskId: String)

    suspend fun updateTask(updatedTask: Task): Task
}

