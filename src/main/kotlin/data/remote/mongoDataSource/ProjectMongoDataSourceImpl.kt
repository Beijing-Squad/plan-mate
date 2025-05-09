package data.remote.mongoDataSource

import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.ProjectMongoDataSource
import logic.entities.Project
import logic.entities.exceptions.ProjectNotFoundException
import org.litote.kmongo.coroutine.CoroutineCollection
import java.util.*
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ProjectMongoDataSourceImpl(
    private val collection: CoroutineCollection<Project> = MongoConnection.database.getCollection("projects")
) : ProjectMongoDataSource {
    override suspend fun getAllProjects(): List<Project> = collection.find().toList()

    override suspend fun addProject(project: Project) {
        collection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        val result = collection.deleteOneById(projectId)
        if (result.deletedCount.toInt() == 0) {
            throw ProjectNotFoundException("Project with ID $projectId not found.")
        }
    }

    override suspend fun updateProject(newProject: Project) {
        val result = collection.replaceOneById(newProject.id.toString(), newProject)
        if (result.matchedCount.toInt() == 0) {
            throw ProjectNotFoundException("Project with ID ${newProject.id} not found.")
        }
    }

    override suspend fun getProjectById(projectId: String): Project {
        return collection.findOneById(UUID.fromString(projectId))
            ?: throw ProjectNotFoundException("Project with ID $projectId not found.")
    }
}