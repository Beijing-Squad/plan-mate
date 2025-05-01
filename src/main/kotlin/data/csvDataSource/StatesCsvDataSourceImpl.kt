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

    @OptIn(ExperimentalUuidApi::class)
    override fun getStateById(stateId: String): State {
        return getAllStates()
            .find { it.id.toString() == stateId }
            ?: throw StateNotFoundException("state not found")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun addState(state: State): Boolean {
        if (states.any { it.id == state.id }) {
            throw StateAlreadyExistException("State with id ${state.id} already exists")
        }
        return states.add(state)
    }

    override fun updateState(state: State) {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: State) {
        TODO("Not yet implemented")
    }

}