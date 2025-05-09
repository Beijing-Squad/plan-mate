package data.repository.remoteDataSource

import data.dto.TaskDTO
import logic.entities.Task

interface TaskMongoDBDataSource {

   suspend fun getAllTasks(): List<Task>

    suspend fun getTaskById(taskId: String): TaskDTO

    suspend fun addTask(task: Task)

    suspend fun deleteTask(taskId: String)

    suspend fun updateTask(updatedTask: Task): TaskDTO
}