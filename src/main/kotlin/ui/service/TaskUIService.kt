package ui.service

import logic.entities.Task

interface TaskUIService {
    fun getAllTasks(): List<Task>

    fun getTaskById(taskId: String): Task

    fun addTask(task: Task)

    fun deleteTask(taskId: String)

    fun updateTask(taskId: String): Task
}