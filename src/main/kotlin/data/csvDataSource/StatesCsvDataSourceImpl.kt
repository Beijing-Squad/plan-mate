package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import logic.entities.State

class StatesCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<State>
) : StatesDataSource {
    private val states = csvDataSource.loadAllDataFromFile().toMutableList()

    override fun getAllStates(): List<State> {
        return states.toList()
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