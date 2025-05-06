package data.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.mongoDataSource.mongoConnection.MongoConnection
import data.mongoDataSource.mongoConnection.MongoConnection.database
import data.repository.dataSource.TasksDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.Project
import logic.entities.Task

class TaskMongoDataSourceImpl (
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
    ) : TasksDataSource
{
    private val collection = database.getCollection<Project>("tasks")
    override suspend fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend  fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override suspend  fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend  fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend  fun updateTask(updatedTask: Task): Task {
        TODO("Not yet implemented")
    }
}