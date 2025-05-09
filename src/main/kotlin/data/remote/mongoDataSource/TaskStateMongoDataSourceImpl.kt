package data.remote.mongoDataSource

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskStateDTO
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.TaskStateMongoDBDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.entities.exceptions.StateNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class TaskStateMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : TaskStateMongoDBDataSource {
    private val collection = database.getCollection<TaskStateDTO>("states")

    override suspend fun getAllStates(): List<TaskStateDTO> {
        return collection.find<TaskStateDTO>().toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getStatesByProjectId(projectId: String): List<TaskStateDTO> {
        val projectIdFilter = eq("projectId", projectId)
        return collection.find(projectIdFilter).toList()
    }

    override suspend fun getStateById(stateId: String): TaskStateDTO? {
        val stateIdFilter = eq("id", stateId)
        return collection.find(stateIdFilter).firstOrNull() ?: throw StateNotFoundException()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addState(taskState: TaskStateDTO): Boolean {
        return collection.insertOne(taskState).wasAcknowledged()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateState(taskState: TaskStateDTO): TaskStateDTO {
        val stateIdFilter = eq("stateId", taskState.id)
        val updatedState = combine(
            set("id", taskState.id),
            set("name", taskState.name),
            set("project_id", taskState.projectId)
        )
        return collection.updateOne(filter = stateIdFilter, update = updatedState)
            .takeIf { it.matchedCount > 0 }
            ?.let { taskState }
            ?: throw StateNotFoundException()
    }

    override suspend fun deleteState(taskState: TaskStateDTO): Boolean {
        val stateIdFilter = eq("stateId", taskState.id)

        return collection.deleteOne(stateIdFilter).deletedCount > 0
    }
}