package data.remote.mongoDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.MongoDBDataSource
import kotlinx.coroutines.flow.firstOrNull
import logic.entities.exceptions.InvalidLoginException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoDBDataSourceImpl(
    database: MongoDatabase = MongoConnection.database
) : MongoDBDataSource {

    private val userCollection = database.getCollection<UserDTO>("users")
    private val auditsCollection = database.getCollection<AuditDTO>("audits")
    private val projectCollection = database.getCollection<ProjectDTO>("projects")
    private val statesCollection = database.getCollection<TaskStateDTO>("states")
    private val taskCollection = database.getCollection<TaskStateDTO>("users")

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
        return userCollection.find(query).firstOrNull() ?: throw InvalidLoginException()
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

    override suspend fun getAllProjects(): List<ProjectDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun addProject(project: ProjectDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(newProjects: ProjectDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun getProjectById(projectId: String): ProjectDTO {
        TODO("Not yet implemented")
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