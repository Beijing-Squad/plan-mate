package data.remote.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.mongoConnection.MongoConnection
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

    override  fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override   fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override   fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override   fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override   fun updateTask(updatedTask: Task): Task {
        TODO("Not yet implemented")
    }
}