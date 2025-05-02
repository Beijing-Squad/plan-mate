package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import logic.entities.State
import kotlin.uuid.ExperimentalUuidApi

class StatesCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<State>
) : StatesDataSource {
    private val states = getAllStates().toMutableList()

    override fun getAllStates(): List<State> {
        return csvDataSource.loadAllDataFromFile()
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        return getAllStates()
            .filter { it.projectId == projectId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getStateById(stateId: String): State? {
        return getAllStates()
            .find { it.id == stateId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun addState(state: State): Boolean {
        return states.add(state).also { isAdded ->
            if (isAdded) {
                csvDataSource.updateFile(states)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateState(newState: State): State {
        return getStateById(newState.id).let { currentState ->
            val updatedState = newState.copy(
                name = newState.name,
                projectId = newState.projectId
            )
            val updatedStates = states.map { if (it == currentState) updatedState else it }
            csvDataSource.updateFile(updatedStates)
            updatedState
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deleteState(state: State): Boolean {
        return states.remove(state)
    }
}