package data.local.csvDataSource

import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.localDataSource.LocalDataSource
import data.utils.hashPassword
import logic.entities.*
import logic.entities.type.UserRole
import logic.exceptions.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LocalDataSourceImpl(
    private val auditCsvDataSource: CsvDataSourceImpl<Audit>,
    private val userCsvDataSource: CsvDataSourceImpl<User>,
    private val projectCsvDataSource: CsvDataSourceImpl<Project>,
    private val taskCsvDataSource: CsvDataSourceImpl<Task>,
    private val taskStateCsvDataSource: CsvDataSourceImpl<TaskState>
) : LocalDataSource {
    private val states = getAllStates().toMutableList()

    //region authentication
    override fun saveUser(username: String, password: String, role: UserRole): Boolean {
        val existingUser = this.getAllUsers()
            .find { it.userName == username }
        if (existingUser != null) {
            throw UserExistsException("User already exists")
        }
        val hashedPassword = hashPassword(password)

        return try {
            val user = createUser(username, hashedPassword, role)
            userCsvDataSource.appendToFile(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAuthenticatedUser(username: String, password: String): User {
        val user = this.getAllUsers()
            .find { it.userName == username }
            ?: throw UserNotFoundException("Invalid username or password")

        val hashedInputPassword = hashPassword(password)
        if (user.password != hashedInputPassword) {
            throw InvalidPasswordException("Invalid password")
        }
        return user
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun createUser(username: String, hashedPassword: String, role: UserRole): User {
        return User(Uuid.random(), username, hashedPassword, role)
    }
    //endregion

    //region audit
    override fun getAllAuditLogs(): List<Audit> {
        return auditCsvDataSource.loadAllDataFromFile()
    }

    override fun addAuditLog(audit: Audit) {
        auditCsvDataSource.appendToFile(audit)
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return getAllAuditLogs().filter { auditLog -> isMatchingProject(auditLog, projectId) }
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return getAllAuditLogs().filter { audit: Audit ->
            audit.entityId == taskId
                    && audit.entityType == Audit.EntityType.TASK
        }
    }

    private fun isMatchingProject(audit: Audit, projectId: String): Boolean {
        return audit.entityId == projectId && audit.entityType == Audit.EntityType.PROJECT
    }
    //endregion

    //region project
    override fun getAllProjects(): List<Project> = projectCsvDataSource.loadAllDataFromFile()

    override fun addProject(project: Project) = projectCsvDataSource.appendToFile(project)

    override fun deleteProject(projectId: String) = projectCsvDataSource.deleteById(projectId)

    override fun updateProject(newProjects: Project) = projectCsvDataSource.updateItem(newProjects)

    override fun getProjectById(projectId: String): Project = projectCsvDataSource.getById(projectId)
    //endregion

    //region task state
    override fun addState(taskState: TaskState): Boolean {
        return try {
            taskStateCsvDataSource.appendToFile(taskState)
            true
        } catch (e: StateException) {
            false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deleteState(taskState: TaskState): Boolean {
        return try {
            taskStateCsvDataSource.deleteById(taskState.id.toString())
            true
        } catch (e: StateException) {
            false
        }
    }

    override fun getAllStates(): List<TaskState> {
        return taskStateCsvDataSource.loadAllDataFromFile()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getStateById(stateId: String): TaskState {
        return getAllStates()
            .find { it.id.toString() == stateId }
            ?: throw StateNotFoundException()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getStatesByProjectId(projectId: String): List<TaskState> {
        return getAllStates()
            .filter { it.projectId.toString() == projectId }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateState(taskState: TaskState): TaskState {
        return getStateById(taskState.id.toString()).let { currentState ->
            val updatedState = currentState.copy(
                name = taskState.name,
                projectId = taskState.projectId
            )
            val updatedStates = states.map { if (it == currentState) updatedState else it }
            taskStateCsvDataSource.updateFile(updatedStates)
            updatedState
        }
    }
    //endregion

    //region task
    override fun getAllTasks(): List<Task> {
        return taskCsvDataSource.loadAllDataFromFile()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getTaskById(taskId: String): Task {

        val tasks = taskCsvDataSource.loadAllDataFromFile()
        return tasks.find { it.id.toString() == taskId }
            ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    override fun addTask(task: Task) = taskCsvDataSource.appendToFile(task)


    override fun deleteTask(taskId: String) = taskCsvDataSource.deleteById(taskId)

    @OptIn(ExperimentalUuidApi::class)
    override fun updateTask(updatedTask: Task): Task {

        val tasks = taskCsvDataSource.loadAllDataFromFile().toMutableList()

        val taskIndex = tasks.indexOfFirst { it.id.toString() == updatedTask.id.toString() }
        if (updatedTask.title.isEmpty()) throw InvalidInputException("Task title cannot be empty")
        if (taskIndex == -1) throw TaskNotFoundException("Task with ID ${updatedTask.id} not found")

        tasks[taskIndex] = updatedTask
        taskCsvDataSource.updateFile(tasks)
        return updatedTask
    }
    //endregion

    //region user
    override fun getAllUsers(): List<User> {
        return userCsvDataSource
            .loadAllDataFromFile()
            .toMutableList()
            .toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserByUserId(userId: String): User {
        return getAllUsers()
            .find { it.id.toString() == userId }
            ?: throw UserNotFoundException()

    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateUser(user: User): User {
        val currentUser = getUserByUserId(user.id.toString())
        val passwordToUse = if (user.password != currentUser.password) {
            hashPassword(user.password)
        } else {
            currentUser.password
        }
        val updatedUser = currentUser
            .copy(userName = user.userName, password = passwordToUse)
        userCsvDataSource.updateItem(updatedUser)
        return updatedUser
    }
    //endregion
}