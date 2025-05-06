package data.repository.dataSource

import logic.entities.TaskState

interface StatesDataSource {

    fun getAllStates(): List<TaskState>

    fun getStatesByProjectId(projectId: String): List<TaskState>

    fun getStateById(stateId: String): TaskState

    fun addState(taskState: TaskState): Boolean

    fun updateState(taskState: TaskState): TaskState

    fun deleteState(taskState: TaskState): Boolean

}