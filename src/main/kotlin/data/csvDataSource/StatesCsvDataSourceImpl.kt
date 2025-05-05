package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import logic.entities.State
import logic.entities.exceptions.StateException
import logic.entities.exceptions.StateNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class StatesCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<State>
) : StatesDataSource {
    private val states = getAllStates().toMutableList()

    @OptIn(ExperimentalUuidApi::class)
    override fun addState(state: State): Boolean {
        return try {
            csvDataSource.appendToFile(state)
            true
        } catch (e: StateException) {
            false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deleteState(state: State): Boolean {
        return try {
            csvDataSource.deleteById(state.id)
            true
        } catch (e: StateException) {
            false
        }
    }

    override fun getAllStates(): List<State> {
        return csvDataSource.loadAllDataFromFile()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getStateById(stateId: String): State {
        return getAllStates()
            .find { it.id == stateId }
            ?: throw StateNotFoundException()
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        return getAllStates()
            .filter { it.projectId == projectId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateState(newState: State): State {
        return getStateById(newState.id).let { currentState ->
            val updatedState = currentState.copy(
                name = newState.name,
                projectId = newState.projectId
            )
            val updatedStates = states.map { if (it == currentState) updatedState else it }
            csvDataSource.updateFile(updatedStates)
            updatedState
        }
    }
}