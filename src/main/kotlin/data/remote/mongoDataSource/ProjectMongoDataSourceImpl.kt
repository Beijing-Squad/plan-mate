package data.remote.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.ProjectDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.Project

class ProjectMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : ProjectDataSource {
    private val collection = database.getCollection<Project>("projects")

    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun addProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override fun updateProject(newProjects: Project) {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: String): Project {
        TODO("Not yet implemented")
    }
}