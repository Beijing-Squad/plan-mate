package data.remote.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskStateDTO
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.StatesDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.Project
import logic.entities.TaskState

class StateMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : StatesDataSource {
    private val collection = database.getCollection<Project>("states")

    override fun getAllStates(): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun getStatesByProjectId(projectId: String): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: String): TaskState {
        TODO("Not yet implemented")
    }

    override fun addState(state: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateState(state: TaskState): TaskState {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: TaskState): Boolean {
        TODO("Not yet implemented")
    }
}