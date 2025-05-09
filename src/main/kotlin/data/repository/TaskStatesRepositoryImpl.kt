package data.repository

import data.repository.mapper.toTaskStateDto
import data.repository.mapper.toTaskStateEntity
import data.repository.remoteDataSource.TaskStateMongoDBDataSource
import logic.entities.TaskState
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class TaskStatesRepositoryImpl(
    private val taskStateDataSource: TaskStateMongoDBDataSource
) : StatesRepository {

    override suspend fun getAllStates(): List<TaskState> {
        return taskStateDataSource.getAllStates()
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getStatesByProjectId(projectId: String): List<TaskState> {
        return taskStateDataSource.getStatesByProjectId(projectId)
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getStateById(stateId: String): TaskState {
        return toTaskStateEntity(
            taskStateDataSource.getStateById(stateId)
                ?: throw StateNotFoundException()
        )
    }

    override suspend fun addState(taskState: TaskState): Boolean {
        return taskStateDataSource.addState(toTaskStateDto(taskState))
    }

    override suspend fun updateState(taskState: TaskState): TaskState {
        val stateDTO = toTaskStateDto(taskState)
        return toTaskStateEntity(taskStateDataSource.updateState(stateDTO))
    }

    override suspend fun deleteState(taskState: TaskState): Boolean {
        return taskStateDataSource.deleteState(toTaskStateDto(taskState))
    }
}