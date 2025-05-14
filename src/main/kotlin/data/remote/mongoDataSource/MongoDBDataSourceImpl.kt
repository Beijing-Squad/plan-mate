package data.remote.mongoDataSource

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.dto.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.RemoteDataSource
import data.utils.hashPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import logic.exceptions.InvalidLoginException
import logic.exceptions.StateNotFoundException
import logic.exceptions.UserNotFoundException
import logic.entity.Audit
import logic.exceptions.*
import org.bson.conversions.Bson
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MongoDBDataSourceImpl(
    database: MongoDatabase = MongoConnection.database
) : RemoteDataSource {

    private val userCollection = database.getCollection<UserDto>("users")
    private val auditsCollection = database.getCollection<AuditDto>("audits")
    private val projectCollection = database.getCollection<ProjectDto>("projects")
    private val statesCollection = database.getCollection<TaskStateDto>("states")
    private val taskCollection = database.getCollection<TaskDto>("tasks")

    //region authentication operations
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
        val query = and(
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
                eq("entityId", projectId),
                eq("entityType", Audit.EntityType.PROJECT.name)
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
        projectCollection.findOneAndDelete(eq("_id", projectId))
    }

    override suspend fun updateProject(newProjects: ProjectDto) {
        projectCollection.replaceOne(eq("_id", newProjects.id), newProjects)
    }

    override suspend fun getProjectById(projectId: String): ProjectDto {
        return projectCollection.find(eq("_id", projectId)).first()
    }

    private suspend fun isProjectExists(projectId: String): Boolean {
        return projectCollection
            .find(eq("_id", projectId))
            .firstOrNull() != null
    }
    //endregion

    //region task operations
    override suspend fun getAllTasks(): List<TaskDto> {
        return taskCollection.find<TaskDto>().toList()
    }

    override suspend fun getTaskById(taskId: String): TaskDto {
        val taskIdFilter = eq("_id", taskId)
        return taskCollection.find<TaskDto>(taskIdFilter).first()
    }

    override suspend fun addTask(task: TaskDto) {
        taskCollection.insertOne(task)
    }
    override suspend fun deleteTask(taskId: String) {
        val taskIdFilter = eq("_id", taskId)
        taskCollection.findOneAndDelete(taskIdFilter)
    }

    override suspend fun updateTask(updatedTask: TaskDto): TaskDto {
        val taskIdFilter = eq("_id", updatedTask.id)
        taskCollection.replaceOne(taskIdFilter, updatedTask)
        return updatedTask
    }
    //endregion

    //region taskState operations
    override suspend fun addTaskState(taskState: TaskStateDto): Boolean {
        if (isTaskStateExists(taskState.id)) throw StateAlreadyExistException()

        return statesCollection.insertOne(taskState).wasAcknowledged()
    }

    override suspend fun deleteTaskState(taskStateId: String): Boolean {
        if (!isTaskStateExists(taskStateId)) throw StateNotFoundException()

        return statesCollection.deleteOne(eq("_id", taskStateId)).deletedCount > 0
    }

    override suspend fun getAllTaskStates(): List<TaskStateDto> {
        return statesCollection.find().toList()
    }

    override suspend fun getTaskStatesByProjectId(projectId: String): List<TaskStateDto> {
        if (!isProjectExists(projectId)) throw ProjectNotFoundException()
        val projectIdFilter = eq("projectId", projectId)
        return statesCollection.find(projectIdFilter).toList()
    }

    override suspend fun getTaskStateById(taskStateId: String): TaskStateDto {
        val stateIdFilter = eq("_id", taskStateId)
        return statesCollection.find(stateIdFilter).firstOrNull() ?: throw StateNotFoundException()
    }

    override suspend fun updateTaskState(taskState: TaskStateDto): Boolean {
        if (!isTaskStateExists(taskState.id)) throw StateNotFoundException()
        if (!isProjectExists(taskState.projectId)) throw ProjectNotFoundException()
        val stateIdFilter = eq("_id", taskState.id)

        return statesCollection.replaceOne(stateIdFilter, taskState).wasAcknowledged()
    }

    private suspend fun isTaskStateExists(stateId: String): Boolean {
        val stateIdFilter = eq("_id", stateId)
        val existingState = statesCollection.find<TaskStateDto>(stateIdFilter).firstOrNull()
        return existingState != null
    }
    //endregion

    //region user operations
    override suspend fun getAllUsers(): List<UserDto> {
        return withContext(Dispatchers.IO) {
            userCollection.find().toList()
        }
    }

    override suspend fun getUserByUserId(userId: String): UserDto {
        return getAllUsers()
            .find { it.id == userId }
            ?: throw UserNotFoundException()
    }

    override suspend fun updateUser(user: UserDto): UserDto {
        val filters = eq("_id", user.id)
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
            val result = userCollection.updateOne(filters, combine(updates))
            require(result.matchedCount.toInt() != 0) { throw UserNotFoundException() }
        }
    }

    //endregion
}