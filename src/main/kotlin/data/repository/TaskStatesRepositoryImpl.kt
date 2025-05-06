package data.repository

import data.repository.dataSource.StatesDataSource
import logic.entities.State
import logic.repository.StatesRepository

class TaskStatesRepositoryImpl(
    private val stateDataSource: StatesDataSource
) : StatesRepository{

    override fun getAllStates(): List<State> {
        return stateDataSource.getAllStates()
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        return stateDataSource.getStatesByProjectId(projectId)
    }

    override fun getStateById(stateId: String): State {
        return stateDataSource.getStateById(stateId)
    }

    override fun addState(state: State): Boolean {
        return stateDataSource.addState(state)
    }

    override fun updateState(state: State): State {
        return stateDataSource.updateState(state)
    }

    override fun deleteState(state: State): Boolean {
        return stateDataSource.deleteState(state)
    }

}

