package data.repository.dataSource

import logic.entities.State

interface StatesDataSource{

    fun getAllStates(): List<State>

    fun getStatesByProjectId(projectId: String): List<State>

    fun getStateById(stateId: String): State

    fun addState(state: State): Boolean

    fun updateState(state: State): Boolean

    fun deleteState(state: State)

}