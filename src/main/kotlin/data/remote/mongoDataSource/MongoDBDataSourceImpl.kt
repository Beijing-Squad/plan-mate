package data.remote.mongoDataSource

import com.mongodb.MongoTimeoutException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.mapper.toTaskDTO
import data.repository.remoteDataSource.MongoDBDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import logic.entities.Task
import logic.entities.exceptions.InvalidLoginException
import logic.entities.exceptions.TaskNotFoundException
import logic.entities.exceptions.StateNotFoundException
import logic.entities.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoDBDataSourceImpl(
    database: MongoDatabase = MongoConnection.database,
    private val validationUserDataSource: ValidationUserDataSource,
    private val passwordHashingDataSource: PasswordHashingDataSource
) : MongoDBDataSource {

    private val userCollection = database.getCollection<UserDTO>("users")
    private val auditsCollection = database.getCollection<AuditDTO>("audits")
    private val projectCollection = database.getCollection<ProjectDTO>("projects")
    private val statesCollection = database.getCollection<TaskStateDTO>("states")
    private val taskCollection = database.getCollection<TaskDTO>("tasks")

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
    override suspend fun getAllProjects(): List<ProjectDTO> = projectCollection.find().toList()

    override suspend fun addProject(project: ProjectDTO) {
        projectCollection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        projectCollection.findOneAndDelete(eq("id", projectId))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateProject(newProjects: ProjectDTO) {
        projectCollection.replaceOne(eq("id", newProjects.id.toString()), newProjects)
    }

    override suspend fun getProjectById(projectId: String): ProjectDTO {
        return projectCollection.find(eq("id", projectId)).first()
    }


    override suspend fun getAllTasks(): List<TaskDTO> {
        return taskCollection.find<TaskDTO>().toList()
    }


    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTaskById(taskId: String): TaskDTO {

        val queryParams = Filters.eq("_id", taskId)
        val task = taskCollection.find<Task>(queryParams).limit(1).firstOrNull()
            ?: throw TaskNotFoundException("Task with id $taskId not found")
        return toTaskDTO(task)
    }

    override suspend fun addTask(task: TaskDTO) {
        try {
            taskCollection.insertOne(task)
        } catch (e: Exception) {
            throw RuntimeException("Failed to add task: ${e.message}", e)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteTask(taskId: String) {
        val queryParams = Filters.eq("_id", taskId)
        taskCollection.findOneAndDelete(queryParams)
         ?: throw TaskNotFoundException("Task with id $taskId not found")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateTask(updatedTask: TaskDTO): TaskDTO {
        val queryParams = Filters.eq("_id", updatedTask.id)
        val updateParams = Updates.combine(
            Updates.set("project_id", updatedTask.projectId),
            Updates.set("title", updatedTask.title),
            Updates.set("description", updatedTask.description),
            Updates.set("created_by", updatedTask.createdBy),
            Updates.set("state_id", updatedTask.stateId),
            Updates.set("created_at", updatedTask.createdAt),
            Updates.set("updated_at", updatedTask.updatedAt)
        )
        val result = taskCollection.updateOne(queryParams, updateParams)
        if (result.matchedCount == 0L) {
            throw TaskNotFoundException("Task with id ${updatedTask.id} not found")
        }
        return updatedTask
    }


    override suspend fun getAllStates(): List<TaskStateDTO> {
        return statesCollection.find<TaskStateDTO>().toList()
    }

    override suspend fun getTaskStatesByProjectId(projectId: String): List<TaskStateDTO> {
        val projectIdFilter = eq("projectId", projectId)
        return statesCollection.find(projectIdFilter).toList()
    }

    override suspend fun getTaskStateById(stateId: String): TaskStateDTO? {
        val stateIdFilter = eq("id", stateId)
        return statesCollection.find(stateIdFilter).firstOrNull() ?: throw StateNotFoundException()
    }

    override suspend fun addTaskState(taskState: TaskStateDTO): Boolean {
        return statesCollection.insertOne(taskState).wasAcknowledged()
    }

    override suspend fun updateTaskState(taskState: TaskStateDTO): TaskStateDTO {
        val stateIdFilter = eq("stateId", taskState.id)
        val updatedState = combine(
            set("id", taskState.id),
            set("name", taskState.name),
            set("project_id", taskState.projectId)
        )
        return statesCollection.updateOne(filter = stateIdFilter, update = updatedState)
            .takeIf { it.matchedCount > 0 }
            ?.let { taskState }
            ?: throw StateNotFoundException()
    }

    override suspend fun deleteTaskState(taskState: TaskStateDTO): Boolean {
        val stateIdFilter = eq("stateId", taskState.id)

        return statesCollection.deleteOne(stateIdFilter).deletedCount > 0
    }

    override suspend fun getAllUsers(): List<UserDTO> {
        return withContext(Dispatchers.IO) {
            try {
                userCollection.find().toList()
            } catch (e: MongoTimeoutException) {
                println("Database connection failed: ${e.message}")
                emptyList()
            }
        }

    }

    override suspend fun getUserByUserId(userId: String): UserDTO {
        return getAllUsers()
            .find { it.id == userId }
            ?: throw UserNotFoundException()
    }

    override suspend fun updateUser(user: UserDTO): UserDTO {
        val filters = Filters.eq(UserDTO::id.name, user.id)
        val existingUser = userCollection.find(filters).firstOrNull() ?: throw UserNotFoundException()

        validationUserDataSource.validateUsername(user.userName)
        validationUserDataSource.validatePassword(user.password)
        val updates = buildList {
            if (user.userName != existingUser.userName) {
                add(Updates.set(UserDTO::userName.name, user.userName))
            }
            if (passwordHashingDataSource.hashPassword(user.password)
                != passwordHashingDataSource.hashPassword(existingUser.password)
            ) {
                add(Updates.set(UserDTO::password.name, passwordHashingDataSource.hashPassword(user.password)))
            }
        }

        if (updates.isNotEmpty()) {
            val result = userCollection.updateOne(filters, Updates.combine(updates))
            require(result.matchedCount.toInt() != 0) { throw UserNotFoundException() }
        }

        return userCollection.find(filters).firstOrNull() ?: throw UserNotFoundException()
    }
}