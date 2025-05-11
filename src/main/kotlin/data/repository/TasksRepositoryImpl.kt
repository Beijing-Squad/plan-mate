package data.repository

import data.repository.mapper.toTaskDTO
import data.repository.mapper.toTaskEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.Task
import logic.repository.TasksRepository

class TasksRepositoryImpl(
    private val taskRemoteDataSource: RemoteDataSource
) : TasksRepository {

    override suspend fun getAllTasks(): List<Task> {
        return try {
            taskRemoteDataSource.getAllTasks().map { toTaskEntity(it) }
        } catch (e: Exception) {
            throw RuntimeException("Failed to retrieve tasks: ${e.message}", e)
        }
    }

    override suspend fun getTaskById(taskId: String): Task {
        return toTaskEntity(taskRemoteDataSource.getTaskById(taskId))
    }

    override suspend fun addTask(task: Task) {
        taskRemoteDataSource.addTask(toTaskDTO(task))
    }

    override suspend fun deleteTask(taskId: String) {
        taskRemoteDataSource.deleteTask(taskId)
    }

    override suspend fun updateTask(updatedTask: Task): Task {
        val updatedDTO = taskRemoteDataSource.updateTask(toTaskDTO(updatedTask))
        return toTaskEntity(updatedDTO)
    }

}
