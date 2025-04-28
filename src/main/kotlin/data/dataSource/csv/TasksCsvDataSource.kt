package data.dataSource.csv

import data.repository.dataSourceAbstraction.TasksDataSource
import logic.entities.Task
import java.io.File

class TasksCsvDataSource(
    private val file:File
): TasksDataSource {

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

    override fun updateTask(taskId: String): Task {
        TODO("Not yet implemented")
    }

}