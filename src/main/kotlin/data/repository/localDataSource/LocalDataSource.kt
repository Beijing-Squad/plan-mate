package data.repository.localDataSource

import data.remote.mongoDataSource.dto.TaskStateDto
import logic.entities.Audit
import logic.entities.Project
import logic.entities.Task
import logic.entities.User
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

    fun addTaskState(taskState: TaskStateDto): Boolean
    fun deleteTaskState(taskStateId: String): Boolean
    fun getAllTaskStates(): List<TaskStateDto>
    fun getTaskStateById(taskStateId: String): TaskStateDto
    fun getTaskStatesByProjectId(projectId: String): List<TaskStateDto>
    fun updateTaskState(taskState: TaskStateDto): Boolean

    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: String): Task
    fun addTask(task: Task)
    fun deleteTask(taskId: String)
    fun updateTask(updatedTask: Task): Task

    fun getAllUsers(): List<User>
    fun getUserByUserId(userId: String): User
    fun updateUser(user: User): User
}