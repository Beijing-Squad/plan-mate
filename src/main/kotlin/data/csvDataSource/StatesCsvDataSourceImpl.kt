package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import logic.entities.State
import kotlin.uuid.ExperimentalUuidApi

class StatesCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<State>
) : StatesDataSource {
    private val states = csvDataSource.loadAllDataFromFile().toMutableList()

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
            .find { it.id.toString() == stateId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun addState(state: State): Boolean {
        if (states.any { it.id == state.id }) {
            throw StateAlreadyExistException("State with id ${state.id} already exists")
        }
        return states.add(state).also { isAdded ->
            if (isAdded) {
                csvDataSource.updateFile(states)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateState(newState: State): State {
        return getStateById(newState.id.toString()).let { currentState ->
            val updatedState = newState.copy(
                name = newState.name,
                projectId = newState.projectId
            )
            states[states.indexOf(currentState)] = updatedState
            csvDataSource.updateFile(states)
            updatedState
        }
    }


    @OptIn(ExperimentalUuidApi::class)
    override fun deleteState(state: State): Boolean {
        val currentState = getStateById(state.id.toString())
        return states.remove(currentState)
    }
}