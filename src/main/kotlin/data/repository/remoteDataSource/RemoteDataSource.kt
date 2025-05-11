package data.repository.remoteDataSource

import data.remote.mongoDataSource.dto.*

interface RemoteDataSource {
    suspend fun saveUser(username: String, password: String, role: String): Boolean
    suspend fun getAuthenticatedUser(username: String, password: String): UserDto

    suspend fun getAllAuditLogs(): List<AuditDto>
    suspend fun addAuditLog(audit: AuditDto)
    suspend fun getAuditLogsByProjectId(projectId: String): List<AuditDto>
    suspend fun getAuditLogsByTaskId(taskId: String): List<AuditDto>

    suspend fun getAllProjects(): List<ProjectDto>
    suspend fun addProject(project: ProjectDto)
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(newProjects: ProjectDto)
    suspend fun getProjectById(projectId: String): ProjectDto

    suspend fun getAllTasks(): List<TaskDto>
    suspend fun getTaskById(taskId: String): TaskDto
    suspend fun addTask(task: TaskDto)
    suspend fun deleteTask(taskId: String)
    suspend fun updateTask(updatedTask: TaskDto): TaskDto

    suspend fun getAllStates(): List<TaskStateDto>
    suspend fun getTaskStatesByProjectId(projectId: String): List<TaskStateDto>
    suspend fun getTaskStateById(stateId: String): TaskStateDto?
    suspend fun addTaskState(taskState: TaskStateDto): Boolean
    suspend fun updateTaskState(taskState: TaskStateDto): TaskStateDto
    suspend fun deleteTaskState(taskState: TaskStateDto): Boolean

    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserByUserId(userId: String): UserDto
    suspend fun updateUser(user: UserDto): UserDto
}