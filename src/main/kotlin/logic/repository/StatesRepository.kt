package logic.repository

import logic.entities.State

interface StatesRepository{
    fun getAllStates(): List<State>

    fun getStatesByProjectId(projectId: String): List<State>

    fun getStateById(stateId: String): State

    fun addState(state: State): Boolean

    fun updateState(state: State)

    fun deleteState(state: State)

}