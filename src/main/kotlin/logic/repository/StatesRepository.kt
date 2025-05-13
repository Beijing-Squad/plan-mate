package logic.repository

import logic.entity.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface StatesRepository{
    suspend fun addTaskState(taskState: TaskState): Boolean

    suspend fun deleteTaskState(taskStateId: Uuid): Boolean

    suspend fun getAllTaskStates(): List<TaskState>

    suspend fun getTaskStatesByProjectId(projectId: Uuid): List<TaskState>

    suspend fun getTaskStateById(taskStateId: Uuid): TaskState

    suspend fun updateTaskState(taskState: TaskState): Boolean

}