package data.repository.remoteDataSource

import data.dto.*

interface RemoteDataSource {
    suspend fun saveUser(username: String, password: String, role: String): Boolean
    suspend fun getAuthenticatedUser(username: String, password: String): UserDTO

    suspend fun getAllAuditLogs(): List<AuditDTO>
    suspend fun addAuditLog(audit: AuditDTO)
    suspend fun getAuditLogsByProjectId(projectId: String): List<AuditDTO>
    suspend fun getAuditLogsByTaskId(taskId: String): List<AuditDTO>

    suspend fun getAllProjects(): List<ProjectDTO>
    suspend fun addProject(project: ProjectDTO)
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(newProjects: ProjectDTO)
    suspend fun getProjectById(projectId: String): ProjectDTO

    suspend fun getAllTasks(): List<TaskDto>
    suspend fun getTaskById(taskId: String): TaskDto
    suspend fun addTask(task: TaskDto)
    suspend fun deleteTask(taskId: String)
    suspend fun updateTask(updatedTask: TaskDto): TaskDto

    suspend fun getAllStates(): List<TaskStateDTO>
    suspend fun getTaskStatesByProjectId(projectId: String): List<TaskStateDTO>
    suspend fun getTaskStateById(stateId: String): TaskStateDTO?
    suspend fun addTaskState(taskState: TaskStateDTO): Boolean
    suspend fun updateTaskState(taskState: TaskStateDTO): TaskStateDTO
    suspend fun deleteTaskState(taskState: TaskStateDTO): Boolean

    suspend fun getAllUsers(): List<UserDTO>
    suspend fun getUserByUserId(userId: String): UserDTO
    suspend fun updateUser(user: UserDTO): UserDTO
}