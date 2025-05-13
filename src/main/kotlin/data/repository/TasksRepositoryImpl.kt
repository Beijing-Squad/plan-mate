package data.repository

import data.repository.mapper.toTaskDto
import data.repository.mapper.toTaskEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entity.Task
import logic.repository.TasksRepository

class TasksRepositoryImpl(
    private val taskRemoteDataSource: RemoteDataSource
) : TasksRepository {

    override suspend fun getAllTasks(): List<Task> {
       return taskRemoteDataSource.getAllTasks().map { it.toTaskEntity() }
    }

    override suspend fun getTaskById(taskId: String): Task {
        return taskRemoteDataSource.getTaskById(taskId).toTaskEntity()
    }

    override suspend fun addTask(task: Task) {
        taskRemoteDataSource.addTask(task.toTaskDto())
    }

    override suspend fun deleteTask(taskId: String) {
        taskRemoteDataSource.deleteTask(taskId)
    }

    override suspend fun updateTask(updatedTask: Task): Task {
        val updatedDto = taskRemoteDataSource.updateTask(updatedTask.toTaskDto())
        return updatedDto.toTaskEntity()
    }
}