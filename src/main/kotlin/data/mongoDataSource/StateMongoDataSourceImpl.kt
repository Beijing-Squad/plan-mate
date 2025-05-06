package data.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.StatesDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.Project
import logic.entities.State

class StateMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : StatesDataSource {
    private val collection = database.getCollection<Project>("states")
    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: String): State? {
        TODO("Not yet implemented")
    }

    override fun addState(state: State): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateState(state: State): State {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: State): Boolean {
        TODO("Not yet implemented")
    }
}