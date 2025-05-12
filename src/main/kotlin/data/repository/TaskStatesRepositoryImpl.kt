package data.repository

import data.repository.mapper.toTaskStateDto
import data.repository.mapper.toTaskStateEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskStatesRepositoryImpl(
    private val taskStateDataSource: RemoteDataSource
) : StatesRepository {

    override suspend fun addTaskState(taskState: TaskState): Boolean {
        return taskStateDataSource.addTaskState(toTaskStateDto(taskState))
    }

    override suspend fun deleteTaskState(taskStateId: Uuid): Boolean {
        return taskStateDataSource.deleteTaskState(taskStateId.toString())
    }


    override suspend fun getAllTaskStates(): List<TaskState> {
        return taskStateDataSource.getAllTaskStates()
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getTaskStatesByProjectId(projectId: Uuid): List<TaskState> {
        return taskStateDataSource.getTaskStatesByProjectId(projectId.toString())
            .map { toTaskStateEntity(it) }
    }

    override suspend fun getTaskStateById(taskstateId: Uuid): TaskState {
        return toTaskStateEntity(
            taskStateDataSource.getTaskStateById(taskstateId.toString())
        )
    }

    override suspend fun updateTaskState(taskState: TaskState): Boolean {
        return taskStateDataSource.updateTaskState(toTaskStateDto(taskState))
    }
}