package ui.screens

import format
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.Task
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.state.GetAllTaskStatesUseCase
import logic.useCases.task.*
import ui.console.SwimlanesRenderer
import ui.enums.TaskBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskManagementScreen(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val swimlanesRenderer: SwimlanesRenderer,
    private val addAudit: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO,
    private val sessionManagerUseCase: SessionManagerUseCase
) : BaseScreen(consoleIO) {

    override val id: String get() = "3"
    override val name: String get() = "Task Screen"

    override fun showOptionService() {
        MenuRenderer.renderMenu(
            """
        ╔══════════════════════════════════════╗
        ║            Task Management           ║
        ╚══════════════════════════════════════╝
        """,
            TaskBoardOption.entries,
            consoleIO
        )
    }

    override fun handleFeatureChoice() {
        while (true) {
            when (getInput()) {
                "1" -> showTasksInSwimlanes()
                "2" -> addTask()
                "3" -> getTaskById()
                "4" -> deleteTaskById()
                "5" -> showAllTasksList()
                "6" -> updateTaskById()
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option\u001B[0m")
            }
            showOptionService()
        }
    }

    fun showTasksInSwimlanes() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (Swimlanes View):\u001B[0m")
        val tasks = getAllTasksUseCase.getAllTasks()
        val states = getAllTaskStatesUseCase.getAllStates()
        swimlanesRenderer.render(tasks, states)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addTask() {
        val currentUser = sessionManagerUseCase.getCurrentUser()

        consoleIO.show("Enter Task Title: ")
        val title = consoleIO.read()

        consoleIO.show("Enter Task Description: ")
        val description = consoleIO.read()

        consoleIO.show("Enter Task State ID: ")
        val stateId = consoleIO.read()

        consoleIO.show("Enter Project ID: ")
        val projectId = consoleIO.read()

        if (currentUser == null) {
            consoleIO.showWithLine("❌ No user is currently logged in.")
            return
        }
        val createdBy = currentUser.userName


        if (title.isNullOrBlank() || stateId.isNullOrBlank() || projectId.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Title, State ID, Project ID, and Creator Name are required.")
            return
        }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val task = Task(
            projectId = projectId,
            title = title,
            description = description ?: "",
            createdBy = createdBy,
            stateId = stateId,
            createdAt = now,
            updatedAt = now
        )

        try {
            addTaskUseCase.addTask(task)
            consoleIO.showWithLine("✅ Task added successfully.")
            sessionManagerUseCase.getCurrentUser()?.userName?.let { userName ->
                val actionDetails = "Admin $userName created task ${task.id} with name '$title' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = currentUser.role,
                        userName = currentUser.userName,
                        action = ActionType.CREATE,
                        entityType = EntityType.TASK,
                        entityId = task.id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Failed to add task: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun showAllTasksList() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (List View):\u001B[0m")
        val tasks = getAllTasksUseCase.getAllTasks()

        if (tasks.isEmpty()) {
            consoleIO.showWithLine("⚠️ No tasks available.")
            return
        }

        tasks.forEach { task ->
            consoleIO.showWithLine(
                """
            ╭────────────────────────────────────────╮            
            │ ID: ${task.id}
            │ Title: ${task.title}
            │ Description: ${task.description}
            │ State ID: ${task.stateId}
            │ Created By: ${task.createdBy}
            │ Created At: ${task.createdAt.format()}
            │ Updated At: ${task.updatedAt.format()}
            ╰────────────────────────────────────────╯
        """.trimIndent()
            )
        }
    }

    fun getTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find Task by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID: \u001B[0m")
        val id = consoleIO.read()

        try {
            val task = getTaskByIdUseCase.getTaskById(id ?: "")
            consoleIO.showWithLine(
                """
            ╭─────────────────────────────────────────╮           
            │ ID: ${task.id}
            │ Title: ${task.title}
            │ Description: ${task.description}
            │ State ID: ${task.stateId}
            │ Created By: ${task.createdBy}
            │ Created At: ${task.createdAt.format()}
            │ Updated At: ${task.updatedAt.format()}
            ╰─────────────────────────────────────────╯
            """.trimIndent()
            )
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message ?: "Task not found."}\u001B[0m")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔄 Update Task\u001B[0m")
        consoleIO.show("Enter Task ID to update: ")
        val id = consoleIO.read()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

        try {
            val existingTask = getTaskByIdUseCase.getTaskById(id)

            consoleIO.show("Enter New Title [${existingTask.title}]: ")
            val newTitleInput = consoleIO.read()
            val newTitle = newTitleInput?.takeIf { it.isNotBlank() } ?: existingTask.title

            consoleIO.show("Enter New Description [${existingTask.description}]: ")
            val newDescriptionInput = consoleIO.read()
            val newDescription = newDescriptionInput?.takeIf { it.isNotBlank() } ?: existingTask.description

            val taskToUpdate = existingTask.copy(
                title = newTitle,
                description = newDescription,
                updatedAt = now
            )

            val updatedTask = updateTaskUseCase.updateTask(taskToUpdate)

            consoleIO.showWithLine("✅ Task updated successfully:\n📌 Title: ${updatedTask.title}, 📝 Description: ${updatedTask.description}")
            sessionManagerUseCase.getCurrentUser()?.userName?.let { userName ->
                val actionDetails = "Admin $userName updated task ${taskToUpdate.id} with name '$newTitle' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = sessionManagerUseCase.getCurrentUser()!!.role,
                        userName = sessionManagerUseCase.getCurrentUser()!!.userName,
                        action = ActionType.UPDATE,
                        entityType = EntityType.TASK,
                        entityId = updatedTask.id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Failed to update task: ${e.message}")
        }
    }

    fun deleteTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🗑️ Delete Task\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID to delete: \u001B[0m")
        val id = consoleIO.read()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val title = getTaskByIdUseCase.getTaskById(id ?: "").title
        try {
            deleteTaskUseCase.deleteTask(id ?: "")
            consoleIO.showWithLine("✅ Task deleted successfully.")
            sessionManagerUseCase.getCurrentUser()?.userName?.let { userName ->
                val actionDetails = "Admin $userName deleted task $id with name '$title' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = sessionManagerUseCase.getCurrentUser()!!.role,
                        userName = sessionManagerUseCase.getCurrentUser()!!.userName,
                        action = ActionType.DELETE,
                        entityType = EntityType.TASK,
                        entityId = id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Error deleting task: ${e.message}")
        }
    }
}
