package logic.repository

import logic.entity.Task

interface TasksRepository{
    suspend fun getAllTasks(): List<Task>

     suspend fun  getTaskById(taskId: String): Task

     suspend fun  addTask(task: Task)

     suspend fun  deleteTask(taskId: String)

     suspend fun  updateTask(updatedTask: Task): Task
}