package data.remote.mongoDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.TaskDTO
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.mapper.toTaskDTO
import data.repository.remoteDataSource.TaskMongoDBDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.entities.Task
import logic.entities.exceptions.TaskNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class TaskMongoDataSourceImpl(
    database: MongoDatabase = MongoConnection.database
) : TaskMongoDBDataSource {

    private val collection = database.getCollection<Task>("tasks")

    override suspend fun getAllTasks(): List<Task> {
        return collection.find<Task>().toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTaskById(taskId: String): TaskDTO {
        val queryParams = Filters.eq("_id", taskId)
        val task = collection.find<Task>(queryParams).limit(1).firstOrNull()
            ?: throw TaskNotFoundException("Task with id $taskId not found")
        return toTaskDTO(task)
    }

    override suspend fun addTask(task: Task) {
        collection.insertOne(task).also {
            println("Task added with id - ${it.insertedId}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteTask(taskId: String) {
        val queryParams = Filters.eq("_id", taskId)
        collection.findOneAndDelete(queryParams)?.also {
            println("Task deleted: $it")
        } ?: throw TaskNotFoundException("Task with id $taskId not found")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateTask(updatedTask: Task): TaskDTO {
        val queryParams = Filters.eq("_id", updatedTask.id.toString())
        val updateParams = Updates.combine(
            Updates.set("project_id", updatedTask.projectId),
            Updates.set("title", updatedTask.title),
            Updates.set("description", updatedTask.description),
            Updates.set("created_by", updatedTask.createdBy),
            Updates.set("state_id", updatedTask.stateId),
            Updates.set("created_at", updatedTask.createdAt),
            Updates.set("updated_at", updatedTask.updatedAt)
        )
        val result = collection.updateOne(queryParams, updateParams)
        if (result.matchedCount == 0L) {
            throw TaskNotFoundException("Task with id ${updatedTask.id} not found")
        }
        println("Task updated: ${result.modifiedCount} document(s) modified")
        return toTaskDTO(updatedTask)
    }
}
