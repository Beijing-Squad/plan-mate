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
        return getAllStates()
            .filter { it.projectId == projectId }
    }

    override fun getStateById(stateId: String): State {
        return getAllStates()
            .find { it.id.toString() == stateId }
            ?: throw StateNotFoundException("state not found")
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