package ui.screens

import logic.factories.TaskFactory
import logic.useCases.state.GetAllStatesUseCase
import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import ui.console.SwimlanesRenderer
import ui.enums.TaskBoardOption
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO

class TaskManagementScreen(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllStatesUseCase: GetAllStatesUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val swimlanesRenderer: SwimlanesRenderer,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {

    override val id: String get() = "3"
    override val name: String get() = "Task Board"

    override fun showOptionService() {
        consoleIO.showWithLine(
            "\n\u001B[36m╔════════════════════════════╗\n" +
                    "║        Task Board         ║\n" +
                    "╚════════════════════════════╝"
        )
        TaskBoardOption.entries.forEach {
            consoleIO.showWithLine("${it.code}. ${it.description}")
        }
        consoleIO.show("\u001B[32mEnter option:\u001B[0m ")
    }

    override fun handleFeatureChoice() {
        when (getInput()) {
            "1" -> showTasksInSwimlanes()
            "2" -> addTask()
            "3" -> getTaskById()
            "4" -> deleteTaskById()
            "0" -> return
            else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option\u001B[0m")
        }
    }
    fun showTasksInSwimlanes() {
        val tasks = getAllTasksUseCase.getAllTasks()
        val states = getAllStatesUseCase.getAllState()
        swimlanesRenderer.render(tasks, states)
    }
    fun addTask() {
        consoleIO.show("Enter Task Title: ")
        val title = consoleIO.read()

        consoleIO.show("Enter Task Description: ")
        val description = consoleIO.read()

        consoleIO.show("Enter Task State ID: ")
        val stateId = consoleIO.read()

        consoleIO.show("Enter Project ID: ")
        val taskId = consoleIO.read()

        consoleIO.show("Enter Creator Name: ")
        val createdBy = consoleIO.read()

        if (title.isNullOrBlank() || stateId.isNullOrBlank() || taskId.isNullOrBlank() || createdBy.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Title, State ID, Project ID, and Creator Name are required.")
            return
        }
        val task = TaskFactory.create(taskId, title, description ?: "", createdBy, stateId)

        try {
            addTaskUseCase.addTask(task)
            consoleIO.showWithLine("✅ Task added successfully.")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Failed to add task: ${e.message}")
        }
    }
    fun getTaskById() {
        consoleIO.show("Enter Task ID: ")
        val id = consoleIO.read()
        try {
            val task = getTaskByIdUseCase.getTaskById(id ?: "")
            consoleIO.showWithLine("✅ Task Found:\n$task")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message ?: "Task not found."}")
        }
    }
    fun deleteTaskById() {
        consoleIO.show("Enter Task ID to delete: ")
        val id = consoleIO.read()

        try {
            deleteTaskUseCase.deleteTask(id ?: "")
            consoleIO.showWithLine("✅ Task deleted successfully.")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Error deleting task: ${e.message}")
        }
    }
}
