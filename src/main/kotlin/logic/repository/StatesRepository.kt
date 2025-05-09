package logic.repository

import logic.entities.TaskState

interface StatesRepository{
    suspend fun getAllStates(): List<TaskState>

    suspend fun getStatesByProjectId(projectId: String): List<TaskState>

    suspend fun getStateById(stateId: String): TaskState

    suspend fun addState(taskState: TaskState): Boolean

    suspend fun updateState(taskState: TaskState): TaskState

    suspend fun deleteState(taskState: TaskState): Boolean

}