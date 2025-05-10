package data.repository.localDataSource

import logic.entities.Task

interface TasksDataSource {

     fun getAllTasks(): List<Task>

     fun getTaskById(taskId: String): Task

     fun addTask(task: Task)

     fun deleteTask(taskId: String)

     fun updateTask(updatedTask: Task): Task
}

