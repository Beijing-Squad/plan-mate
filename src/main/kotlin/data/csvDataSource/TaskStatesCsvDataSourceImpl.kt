package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.StatesDataSource
import logic.entities.TaskState
import logic.entities.exceptions.StateException
import logic.entities.exceptions.StateNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class TaskStatesCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<TaskState>
) : StatesDataSource {
    private val states = getAllStates().toMutableList()

    @OptIn(ExperimentalUuidApi::class)
    override fun addState(taskState: TaskState): Boolean {
        return try {
            csvDataSource.appendToFile(taskState)
            true
        } catch (e: StateException) {
            false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deleteState(taskState: TaskState): Boolean {
        return try {
            csvDataSource.deleteById(taskState.id.toString())
            true
        } catch (e: StateException) {
            false
        }
    }

    override fun getAllStates(): List<TaskState> {
        return csvDataSource.loadAllDataFromFile()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getStateById(stateId: String): TaskState {
        return getAllStates()
            .find { it.id.toString() == stateId }
            ?: throw StateNotFoundException()
    }

    override fun getStatesByProjectId(projectId: String): List<TaskState> {
        return getAllStates()
            .filter { it.projectId == projectId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateState(taskState: TaskState): TaskState {
        return getStateById(taskState.id.toString()).let { currentState ->
            val updatedState = currentState.copy(
                name = taskState.name,
                projectId = taskState.projectId
            )
            val updatedStates = states.map { if (it == currentState) updatedState else it }
            csvDataSource.updateFile(updatedStates)
            updatedState
        }
    }
}