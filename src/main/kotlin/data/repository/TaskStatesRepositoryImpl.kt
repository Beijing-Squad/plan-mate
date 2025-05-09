package data.repository

import data.repository.dataSource.StatesDataSource
import logic.entities.TaskState
import logic.repository.StatesRepository

class TaskStatesRepositoryImpl(
    private val stateDataSource: StatesDataSource
) : StatesRepository{

    override fun getAllStates(): List<TaskState> {
        return stateDataSource.getAllStates()
    }

    override fun getStatesByProjectId(projectId: String): List<TaskState> {
        return stateDataSource.getStatesByProjectId(projectId)
    }

    override fun getStateById(stateId: String): TaskState {
        return stateDataSource.getStateById(stateId)
    }

    override fun addState(taskState: TaskState): Boolean {
        return stateDataSource.addState(taskState)
    }

    override fun updateState(taskState: TaskState): TaskState {
        return stateDataSource.updateState(taskState)
    }

    override fun deleteState(taskState: TaskState): Boolean {
        return stateDataSource.deleteState(taskState)
    }

}

