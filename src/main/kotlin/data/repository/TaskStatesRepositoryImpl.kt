package data.repository

import data.repository.mapper.toTaskStateDto
import data.repository.mapper.toTaskStateEntity
import data.repository.remoteDataSource.MongoDBDataSource
import logic.entities.TaskState
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class TaskStatesRepositoryImpl(
    private val taskStateDataSource: MongoDBDataSource
) : StatesRepository {

    override suspend fun getAllStates(): List<TaskState> {
        return taskStateDataSource.getAllStates()
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getStatesByProjectId(projectId: String): List<TaskState> {
        return taskStateDataSource.getTaskStatesByProjectId(projectId)
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getStateById(stateId: String): TaskState {
        return toTaskStateEntity(
            taskStateDataSource.getTaskStateById(stateId)
                ?: throw StateNotFoundException()
        )
    }

    override suspend fun addState(taskState: TaskState): Boolean {
        return taskStateDataSource.addTaskState(toTaskStateDto(taskState))
    }

    override suspend fun updateState(taskState: TaskState): TaskState {
        val stateDTO = toTaskStateDto(taskState)
        return toTaskStateEntity(taskStateDataSource.updateTaskState(stateDTO))
    }

    override suspend fun deleteState(taskState: TaskState): Boolean {
        return taskStateDataSource.deleteTaskState(toTaskStateDto(taskState))
    }
}