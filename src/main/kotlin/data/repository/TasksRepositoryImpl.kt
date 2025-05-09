package data.repository

import data.repository.remoteDataSource.TaskMongoDBDataSource
import data.repository.mapper.toTaskDTO
import data.repository.mapper.toTaskEntity
import logic.entities.Task
import logic.repository.TasksRepository

class TasksRepositoryImpl(
    private val taskMongoDBDataSource: TaskMongoDBDataSource
) : TasksRepository {

    override suspend fun getAllTasks(): List<Task> {
        return try {
            taskMongoDBDataSource.getAllTasks().map { toTaskEntity(it) }
        } catch (e: Exception) {
            throw RuntimeException("Failed to retrieve tasks: ${e.message}", e)
        }
    }

    override suspend fun getTaskById(taskId: String): Task {
        return toTaskEntity(taskMongoDBDataSource.getTaskById(taskId))
    }

    override suspend fun addTask(task: Task) {
        taskMongoDBDataSource.addTask(toTaskDTO(task))
    }

    override suspend fun deleteTask(taskId: String) {
        taskMongoDBDataSource.deleteTask(taskId)
    }

    override suspend fun updateTask(updatedTask: Task): Task {
        val updatedDTO = taskMongoDBDataSource.updateTask(toTaskDTO(updatedTask))
        return toTaskEntity(updatedDTO)
    }

}
