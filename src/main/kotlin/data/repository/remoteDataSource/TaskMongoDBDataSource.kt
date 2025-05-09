package data.repository.remoteDataSource

import data.dto.TaskDTO
import logic.entities.Task

interface TaskMongoDBDataSource {

   suspend fun getAllTasks(): List<TaskDTO>

    suspend fun getTaskById(taskId: String): TaskDTO

    suspend fun addTask(task: TaskDTO)

    suspend fun deleteTask(taskId: String)

    suspend fun updateTask(updatedTask: TaskDTO): TaskDTO
}