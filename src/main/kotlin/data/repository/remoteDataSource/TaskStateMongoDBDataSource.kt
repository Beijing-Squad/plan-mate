package data.repository.remoteDataSource

import data.dto.TaskStateDTO

interface TaskStateMongoDBDataSource {
    suspend fun getAllStates(): List<TaskStateDTO>

    suspend fun getStatesByProjectId(projectId: String): List<TaskStateDTO>

    suspend fun getStateById(stateId: String): TaskStateDTO?

    suspend fun addState(taskState: TaskStateDTO): Boolean

    suspend fun updateState(taskState: TaskStateDTO): TaskStateDTO

    suspend fun deleteState(taskState: TaskStateDTO): Boolean

}