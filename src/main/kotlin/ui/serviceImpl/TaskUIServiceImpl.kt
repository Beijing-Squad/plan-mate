package ui.serviceImpl

import logic.entities.Task
import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import ui.service.ConsoleIOService
import ui.service.TaskUIService
import kotlin.uuid.ExperimentalUuidApi

class TaskUIServiceImpl(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val console: ConsoleIOService
): TaskUIService {
    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }

    override fun addTask(task: Task) {
        addTaskUseCase.addTask(task)
        console.printMessage("Task added successfully.")
    }

    override fun deleteTask(taskId: String) {
        try {
            deleteTaskUseCase.deleteTask(taskId)
            console.printMessage("Task deleted successfully.")
        } catch (e: Exception) {
            console.printMessage("Error deleting task: ${e.message}")
        }
    }

    override fun updateTask(taskId: String): Task {
        TODO("Not yet implemented")
    }
}
