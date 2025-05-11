package data.remote.mongoDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.dto.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.mapper.toTaskDTO
import data.repository.remoteDataSource.RemoteDataSource
import data.utils.hashPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import logic.entities.Audit
import logic.entities.Task
import logic.exceptions.*
import org.bson.conversions.Bson
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoDBDataSourceImpl(
    database: MongoDatabase = MongoConnection.database
) : RemoteDataSource {

    private val userCollection = database.getCollection<UserDto>("users")
    private val auditsCollection = database.getCollection<AuditDto>("audits")
    private val projectCollection = database.getCollection<ProjectDto>("projects")
    private val statesCollection = database.getCollection<TaskStateDto>("states")
    private val taskCollection = database.getCollection<TaskDto>("tasks")

    //region authentication operations
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveUser(
        username: String,
        password: String,
        role: String
    ): Boolean {
        val newUser = UserDto(
            id = Uuid.random().toString(),
            userName = username,
            password = hashPassword(password),
            role = role
        )
        val result = userCollection.insertOne(newUser)
        return result.wasAcknowledged()
    }

    override suspend fun getAuthenticatedUser(username: String, password: String): UserDto {
        val query = Filters.and(
            eq("userName", username), eq(
                "password",
                hashPassword(password)
            )
        )
        return userCollection.find(query).firstOrNull() ?: throw InvalidLoginException()
    }
    //endregion

    //region audit operations
    override suspend fun getAllAuditLogs(): List<AuditDto> {
        return auditsCollection.find().toList()
    }

    override suspend fun addAuditLog(audit: AuditDto) {
        auditsCollection.insertOne(audit)
    }

    override suspend fun getAuditLogsByProjectId(projectId: String): List<AuditDto> {
        return auditsCollection.find(
            and(
                eq<String>("entityId", projectId),
                eq<String>("entityType", Audit.EntityType.PROJECT.name)
            )
        ).toList()
    }

    override suspend fun getAuditLogsByTaskId(taskId: String): List<AuditDto> {
        return auditsCollection.find(
            and(
                eq("entityId", taskId),
                eq("entityType", Audit.EntityType.TASK.name)
            )
        ).toList()
    }
    //endregion

    //region project operations
    override suspend fun getAllProjects(): List<ProjectDto> = projectCollection.find().toList()

    override suspend fun addProject(project: ProjectDto) {
        projectCollection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        projectCollection.findOneAndDelete(eq("id", projectId))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateProject(newProjects: ProjectDto) {
        projectCollection.replaceOne(eq("id", newProjects.id.toString()), newProjects)
    }

    override suspend fun getProjectById(projectId: String): ProjectDto {
        return projectCollection.find(eq("id", projectId)).first()
    }
    //endregion

    //region task operations
    override suspend fun getAllTasks(): List<TaskDto> {
        return taskCollection.find<TaskDto>().toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTaskById(taskId: String): TaskDto {

        val taskIdFilter = Filters.eq("_id", taskId)
        val task = taskCollection.find<Task>(taskIdFilter).limit(1).firstOrNull()
            ?: throw TaskNotFoundException("Task with id $taskId not found")
        return toTaskDTO(task)
    }

    override suspend fun addTask(task: TaskDto) {
        try {
            taskCollection.insertOne(task)
        } catch (e: Exception) {
            throw RuntimeException("Failed to add task: ${e.message}", e)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteTask(taskId: String) {
        val taskIdFilter = Filters.eq("_id", taskId)
        taskCollection.findOneAndDelete(taskIdFilter)
            ?: throw TaskNotFoundException("Task with id $taskId not found")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateTask(updatedTask: TaskDto): TaskDto {
        val taskIdFilter = Filters.eq("_id", updatedTask.id)
        val updateTask = Updates.combine(
            Updates.set("project_id", updatedTask.projectId),
            Updates.set("title", updatedTask.title),
            Updates.set("description", updatedTask.description),
            Updates.set("created_by", updatedTask.createdBy),
            Updates.set("state_id", updatedTask.stateId),
            Updates.set("created_at", updatedTask.createdAt),
            Updates.set("updated_at", updatedTask.updatedAt)
        )
        val result = taskCollection.updateOne(taskIdFilter, updateTask)
        if (result.matchedCount == 0L) {
            throw TaskNotFoundException("Task with id ${updatedTask.id} not found")
        }
        return updatedTask
    }
    //endregion

    //region task state operations
    override suspend fun getAllStates(): List<TaskStateDto> {
        return statesCollection.find<TaskStateDto>().toList()
    }

    override suspend fun getTaskStatesByProjectId(projectId: String): List<TaskStateDto> {
        val projectIdFilter = eq("projectId", projectId)
        return statesCollection.find(projectIdFilter).toList()
    }

    override suspend fun getTaskStateById(stateId: String): TaskStateDto {
        val stateIdFilter = eq("_id", stateId)
        return statesCollection.find(stateIdFilter).firstOrNull() ?: throw StateNotFoundException()
    }

    override suspend fun addTaskState(taskState: TaskStateDto): Boolean {
        return statesCollection.insertOne(taskState).wasAcknowledged()
    }

    override suspend fun updateTaskState(taskState: TaskStateDto): TaskStateDto {
        val stateIdFilter = eq("_id", taskState.id)
        val updatedState = combine(
            set("name", taskState.name),
            set("projectId", taskState.projectId)
        )
        return statesCollection.updateOne(filter = stateIdFilter, update = updatedState)
            .takeIf { it.matchedCount > 0 }
            ?.let { taskState }
            ?: throw StateNotFoundException()
    }

    override suspend fun deleteTaskState(taskState: TaskStateDto): Boolean {
        val stateIdFilter = eq("_id", taskState.id)

        return statesCollection.deleteOne(stateIdFilter).deletedCount > 0
    }
    //endregion

    //region user operations
    override suspend fun getAllUsers(): List<UserDto> {
        return withContext(Dispatchers.IO) {
            userCollection.find().toList()
        }.ifEmpty { throw DataSourceException("Unable to fetch users due to a data source issue. Please try again later.") }
    }

    override suspend fun getUserByUserId(userId: String): UserDto {
        return getAllUsers()
            .find { it.id == userId }
            ?: throw UserNotFoundException()
    }

    override suspend fun updateUser(user: UserDto): UserDto {
        val filters = eq(UserDto::id.name, user.id)
        val existingUser = userCollection.find(filters).firstOrNull() ?: throw UserNotFoundException()
        val updates = buildList {
            addAll(buildUsernameUpdates(user.userName, existingUser.userName))
            addAll(buildPasswordUpdates(user.password, existingUser.password))
        }
        checkUpdateUserIfEmpty(updates, filters)
        return withContext(Dispatchers.IO) {
            userCollection.find(filters).firstOrNull() ?: throw UserNotFoundException()
        }
    }

    private fun buildUsernameUpdates(newUserName: String, existingUserName: String): List<Bson> {
        return if (newUserName != existingUserName) {
            listOf(set(UserDto::userName.name, newUserName))
        } else {
            emptyList()
        }
    }

    private fun buildPasswordUpdates(newPassword: String, existingPassword: String): List<Bson> {
        return if (hashPassword(newPassword)
            != hashPassword(existingPassword)
        ) {
            listOf(set(UserDto::password.name, hashPassword(newPassword)))
        } else {
            emptyList()
        }
    }

    private suspend fun checkUpdateUserIfEmpty(updates: List<Bson>, filters: Bson) {
        if (updates.isNotEmpty()) {
            val result = userCollection.updateOne(filters, Updates.combine(updates))
            require(result.matchedCount.toInt() != 0) { throw UserNotFoundException() }
        }
    }

    //endregion
}