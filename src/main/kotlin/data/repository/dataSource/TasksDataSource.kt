package data.repository.dataSource

import logic.entities.Task

interface TasksDataSource {

    fun getAllTasks(): List<Task>

    fun getTaskById(taskId: String): Task

    fun addTask(task: Task)

    fun deleteTask(taskId: String)

    fun updateTask(taskId: String, updatedTask: Task): Task

}

