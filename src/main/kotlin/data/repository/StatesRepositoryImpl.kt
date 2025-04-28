package data.repository

import data.repository.dataSource.StatesDataSource
import logic.entities.State
import logic.repository.StatesRepository

class StatesRepositoryImpl(
    private val stateDataSource: StatesDataSource
) : StatesRepository{
    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: String): State {
        TODO("Not yet implemented")
    }

    override fun addState(state: State) {
        TODO("Not yet implemented")
    }

    override fun updateState(state: State) {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: State) {
        TODO("Not yet implemented")
    }

}

