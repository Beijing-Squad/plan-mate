package data.repository.localDataSource

import logic.entities.*
import logic.entities.type.UserRole

interface LocalDataSource {

    fun saveUser(username: String, password: String, role: UserRole): Boolean
    fun getAuthenticatedUser(username: String, password: String): User

    fun getAllAuditLogs(): List<Audit>
    fun addAuditLog(audit: Audit)
    fun getAuditLogsByProjectId(projectId: String): List<Audit>
    fun getAuditLogsByTaskId(taskId: String): List<Audit>

    fun getAllProjects(): List<Project>
    fun addProject(project: Project)
    fun deleteProject(projectId: String)
    fun updateProject(newProjects: Project)
    fun getProjectById(projectId: String): Project

    fun getAllStates(): List<TaskState>
    fun getStatesByProjectId(projectId: String): List<TaskState>
    fun getStateById(stateId: String): TaskState
    fun addState(taskState: TaskState): Boolean
    fun updateState(taskState: TaskState): TaskState
    fun deleteState(taskState: TaskState): Boolean

    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: String): Task
    fun addTask(task: Task)
    fun deleteTask(taskId: String)
    fun updateTask(updatedTask: Task): Task

    fun getAllUsers(): List<User>
    fun getUserByUserId(userId: String): User
    fun updateUser(user: User): User
}