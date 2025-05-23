package data.repository.remoteDataSource

import data.dto.*

interface MongoDBDataSource {
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

    suspend fun getAllTasks(): List<TaskDTO>
    suspend fun getTaskById(taskId: String): TaskDTO
    suspend fun addTask(task: TaskDTO)
    suspend fun deleteTask(taskId: String)
    suspend fun updateTask(updatedTask: TaskDTO): TaskDTO

    suspend fun getAllStates(): List<TaskStateDTO>
    suspend fun getStatesByProjectId(projectId: String): List<TaskStateDTO>
    suspend fun getStateById(stateId: String): TaskStateDTO?
    suspend fun addState(taskState: TaskStateDTO): Boolean
    suspend fun updateState(taskState: TaskStateDTO): TaskStateDTO
    suspend fun deleteState(taskState: TaskStateDTO): Boolean

    suspend fun getAllUsers(): List<UserDTO>
    suspend fun getUserByUserId(userId: String): UserDTO
    suspend fun updateUser(user: UserDTO): UserDTO
}