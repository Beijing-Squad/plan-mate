package data.repository.localDataSource

import logic.entity.*
import logic.entity.type.UserRole

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

    fun getAllTaskStates(): List<TaskState>
    fun getTaskStatesByProjectId(projectId: String): List<TaskState>
    fun getTaskStateById(stateId: String): TaskState
    fun addTaskState(taskState: TaskState): Boolean
    fun updateTaskState(taskState: TaskState): TaskState
    fun deleteTaskState(taskState: TaskState): Boolean

    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: String): Task
    fun addTask(task: Task)
    fun deleteTask(taskId: String)
    fun updateTask(updatedTask: Task): Task

    fun getAllUsers(): List<User>
    fun getUserByUserId(userId: String): User
    fun updateUser(user: User): User
}