package data.repository.dataSourceAbstraction

import logic.entities.State

interface StatesDataSource{

    fun getAllStates(): List<State>

    fun getStatesByProjectId(projectId: String): List<State>

    fun getStateById(stateId: String): State

    fun addState(state: State)

    fun updateState(state: State)

    fun deleteState(state: State)

}