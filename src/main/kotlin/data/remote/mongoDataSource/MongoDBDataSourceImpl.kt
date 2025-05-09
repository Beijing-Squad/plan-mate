package data.remote.mongoDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import data.dto.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.MongoDBDataSource
import logic.entities.exceptions.InvalidLoginException
import logic.entities.exceptions.ProjectNotFoundException
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoDBDataSourceImpl(
    database: CoroutineDatabase? = MongoConnection.database
) : MongoDBDataSource {

    private val userCollection = database?.getCollection<UserDTO>("users")?: error("❌ MongoDB database is not connected.")

    private val auditsCollection = database?.getCollection<AuditDTO>("audits")?: error("❌ MongoDB database is not connected.")

    private val projectCollection = database?.getCollection<ProjectDTO>("projects")?: error("❌ MongoDB database is not connected.")
    private val statesCollection = database?.getCollection<TaskStateDTO>("states")?: error("❌ MongoDB database is not connected.")
    private val taskCollection = database?.getCollection<TaskStateDTO>("users")?: error("❌ MongoDB database is not connected.")

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveUser(
        username: String,
        password: String,
        role: String
    ): Boolean {
        val newUser = UserDTO(
            id = Uuid.random().toString(),
            userName = username,
            password = password,
            role = role
        )
        val result = userCollection.insertOne(newUser)
        return result.wasAcknowledged()
    }

    override suspend fun getAuthenticatedUser(username: String, password: String): UserDTO {
        val query = Filters.and(eq("userName", username), eq("password", password))
        return userCollection.find(query).toList().firstOrNull() ?: throw InvalidLoginException()

    }

    override suspend fun getAllAuditLogs(): List<AuditDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun addAuditLog(audit: AuditDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun getAuditLogsByProjectId(projectId: String): List<AuditDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuditLogsByTaskId(taskId: String): List<AuditDTO> {
        TODO("Not yet implemented")
    }
    override suspend fun getAllProjects(): List<ProjectDTO> = projectCollection.find().toList()

    override suspend fun addProject(project: ProjectDTO) {
        projectCollection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        val result = projectCollection.deleteOneById(UUID.fromString(projectId))
        if (result.deletedCount == 0L) {
            throw ProjectNotFoundException("Project with ID $projectId not found.")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateProject(newProjects: ProjectDTO) {
        val result = projectCollection.replaceOneById(newProjects.id, newProjects)
        if (result.matchedCount == 0L) {
            throw ProjectNotFoundException("Project with ID ${newProjects.id} not found.")
        }
    }

    override suspend fun getProjectById(projectId: String): ProjectDTO {
        return projectCollection.findOneById(UUID.fromString(projectId))
            ?: throw ProjectNotFoundException("Project with ID $projectId not found.")
    }

    override suspend fun getAllTasks(): List<TaskDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: String): TaskDTO {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: TaskDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(updatedTask: TaskDTO): TaskDTO {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStates(): List<TaskStateDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getStatesByProjectId(projectId: String): List<TaskStateDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getStateById(stateId: String): TaskStateDTO? {
        TODO("Not yet implemented")
    }

    override suspend fun addState(taskState: TaskStateDTO): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateState(taskState: TaskStateDTO): TaskStateDTO {
        TODO("Not yet implemented")
    }

    override suspend fun deleteState(taskState: TaskStateDTO): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<UserDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUserId(userId: String): UserDTO {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: UserDTO): UserDTO {
        TODO("Not yet implemented")
    }
}