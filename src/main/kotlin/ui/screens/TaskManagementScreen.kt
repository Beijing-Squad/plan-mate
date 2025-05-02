package ui.screens

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import logic.entities.Task
import logic.useCases.state.GetAllStatesUseCase
import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import ui.console.SwimlanesRenderer
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

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
    override val name: String get() = "Task Screen"

    override fun showOptionService() {
        consoleIO.showWithLine(
            """
            ╔════════════════════════════════════════╗
            ║           Task Management              ║
            ╚════════════════════════════════════════╝

            ┌─── Available Options ────────────────────┐
            │                                          │
            │  1. Show All Tasks (Swimlanes)           │
            │  2. Add Task                             │
            │  3. Find Task by ID                      │
            │  4. Delete Task                          │
            │  5. Show All Tasks (List View)           │
            │  0. Exit to Main Menu                    │
            │                                          │
            └──────────────────────────────────────────┘

            """.trimIndent()
        )
        consoleIO.show("\uD83D\uDCA1 Please enter your choice: ")
    }
    override fun handleFeatureChoice() {
        while (true) {
            when (getInput()) {
                "1" -> showTasksInSwimlanes()
                "2" -> addTask()
                "3" -> getTaskById()
                "4" -> deleteTaskById()
                "5" -> showAllTasksList()
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option\u001B[0m")
            }
            showOptionService()
        }
    }

    private fun showTasksInSwimlanes() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (Swimlanes View):\u001B[0m")
        val tasks = getAllTasksUseCase.getAllTasks()
        val states = getAllStatesUseCase.getAllStates()
        swimlanesRenderer.render(tasks, states)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addTask() {
        consoleIO.show("Enter Task Title: ")
        val title = consoleIO.read()

        consoleIO.show("Enter Task Description: ")
        val description = consoleIO.read()

        consoleIO.show("Enter Task State ID: ")
        val stateId = consoleIO.read()

        consoleIO.show("Enter Project ID: ")
        val projectId = consoleIO.read()

        consoleIO.show("Enter Creator Name: ")
        val createdBy = consoleIO.read()

        if (title.isNullOrBlank() || stateId.isNullOrBlank() || projectId.isNullOrBlank() || createdBy.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Title, State ID, Project ID, and Creator Name are required.")
            return
        }

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val task = Task(
            projectId = projectId,
            title = title,
            description = description ?: "",
            createdBy = createdBy,
            stateId = stateId,
            createdAt = today,
            updatedAt = today
        )

        try {
            addTaskUseCase.addTask(task)
            consoleIO.showWithLine("✅ Task added successfully.")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Failed to add task: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun showAllTasksList() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (List View):\u001B[0m")
        val tasks = getAllTasksUseCase.getAllTasks()

        if (tasks.isEmpty()) {
            consoleIO.showWithLine("⚠️ No tasks available.")
            return
        }

        tasks.forEach { task ->
            consoleIO.showWithLine(
                """
            ────────────────────────────────────────
            🆔 ID: ${task.id}
            📌 Title: ${task.title}
            📝 Description: ${task.description}
            🗂 State ID: ${task.stateId}
            👤 Created By: ${task.createdBy}
            📅 Created At: ${task.createdAt}
            🔄 Updated At: ${task.updatedAt}
            """.trimIndent()
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun getTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find Task by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID: \u001B[0m")
        val id = consoleIO.read()

        try {
            val task = getTaskByIdUseCase.getTaskById(id ?: "")
            consoleIO.showWithLine(
                """
                ╭────────────────────────────╮
                │ ✅ Task Found:             
                │ ID: ${task.id}
                │ Title: ${task.title}
                │ Description: ${task.description}
                │ State ID: ${task.stateId}
                │ Created By: ${task.createdBy}
                ╰────────────────────────────╯
                """.trimIndent()
            )
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message ?: "Task not found."}\u001B[0m")
        }
    }

    private fun deleteTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🗑️ Delete Task\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID to delete: \u001B[0m")
        val id = consoleIO.read()

        try {
            deleteTaskUseCase.deleteTask(id ?: "")
            consoleIO.showWithLine("\u001B[32m✅ Task deleted successfully.\u001B[0m")
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Error deleting task: ${e.message}\u001B[0m")
        }
    }
}
