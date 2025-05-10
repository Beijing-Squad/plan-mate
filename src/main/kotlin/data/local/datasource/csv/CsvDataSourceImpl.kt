package data.local.datasource.csv

import data.repository.dataSource.LocalDataSource
import logic.entities.*

class CsvDataSourceImpl() : LocalDataSource {

    //region authentication operations
    override fun saveUser(username: String, password: String, role: UserRole): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAuthenticatedUser(username: String, password: String): User {
        TODO("Not yet implemented")
    }
    //endregion

    //region audit operations
    override fun getAllAuditLogs(): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun addAuditLog(audit: Audit) {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        TODO("Not yet implemented")
    }
    //endregion

    //region project operations
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun addProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override fun updateProject(newProjects: Project) {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: String): Project {
        TODO("Not yet implemented")
    }
    //endregion

    //region task operations
    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun updateTask(updatedTask: Task): Task {
        TODO("Not yet implemented")
    }
    //endregion

    //region taskState operations
    override fun getAllStates(): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun getTaskStatesByProjectId(projectId: String): List<TaskState> {
        TODO("Not yet implemented")
    }

    override fun getTaskStateById(stateId: String): TaskState {
        TODO("Not yet implemented")
    }

    override fun addTaskState(taskState: TaskState): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateTaskState(taskState: TaskState): TaskState {
        TODO("Not yet implemented")
    }

    override fun deleteTaskState(taskState: TaskState): Boolean {
        TODO("Not yet implemented")
    }
    //endregion

    //region user operations
    override fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getUserByUserId(userId: String): User {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User): User {
        TODO("Not yet implemented")
    }
    //endregion
}