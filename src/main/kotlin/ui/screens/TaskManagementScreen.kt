package ui.screens

import data.remote.mongoDataSource.dto.TaskDto
import data.repository.mapper.toTaskDto
import data.repository.mapper.toTaskEntity
import format
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Audit
import logic.exceptions.TaskAlreadyExistsException
import logic.exceptions.TaskNotFoundException
import logic.exceptions.TaskException
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
    private val sessionManagerUseCase: SessionManagerUseCase,
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
                "1" ->  showTasksInSwimlanes()
                "2" ->  addTask()
                "3" ->  getTaskById()
                "4" ->  deleteTaskById()
                "5" ->  showAllTasksList()
                "6" ->  updateTaskById()
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option\u001B[0m")
            }
            showOptionService()
        }
    }

    fun showTasksInSwimlanes() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (Swimlanes View):\u001B[0m")
        showAnimation("Loading tasks...") {
            try {
                val tasks = getAllTasksUseCase.getAllTasks()
                val states = getAllTaskStatesUseCase.getAllTaskStates()
                if (tasks.isEmpty()) {
                    consoleIO.showWithLine("⚠️ No tasks available.")
                }
                consoleIO.showWithLine("")
                swimlanesRenderer.render(tasks, states)
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks: ${e.message}\u001B[0m")
            }
        }
    }

    fun showAllTasksList() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (List View):\u001B[0m")
        showAnimation("Fetching task list...") {
            try {
                val tasks = getAllTasksUseCase.getAllTasks()
                if (tasks.isEmpty()) {
                    consoleIO.showWithLine("⚠️ No tasks available.")
                }
                tasks.forEach { task ->
                    val taskDTO = task.toTaskDto()
                    val createdAt = try {
                        LocalDateTime.parse(taskDTO.createdAt).format()
                    } catch (_: Exception) {
                        taskDTO.createdAt
                    }
                    val updatedAt = try {
                        LocalDateTime.parse(taskDTO.updatedAt).format()
                    } catch (_: Exception) {
                        taskDTO.updatedAt
                    }
                    consoleIO.showWithLine("")
                    consoleIO.showWithLine(
                        """
                        ╭────────────────────────────────────────╮
                        │ ID: ${taskDTO.id}
                        │ Title: ${taskDTO.title}
                        │ Description: ${taskDTO.description}
                        │ State ID: ${taskDTO.stateId}
                        │ Created By: ${taskDTO.createdBy}
                        │ Created At: $createdAt
                        │ Updated At: $updatedAt
                        ╰────────────────────────────────────────╯
                        """.trimIndent()
                    )
                }
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks: ${e.message}\u001B[0m")
            }
        }
    }

    fun getTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find Task by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID: \u001B[0m")
        val id = consoleIO.read()?.trim()

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

        showAnimation("Finding task...") {
            try {
                val taskDTO = getTaskByIdUseCase.getTaskById(id)
                consoleIO.showWithLine("")
                consoleIO.showWithLine(
                    """
                    ╭─────────────────────────────────────────╮
                    │ ID: ${taskDTO.id}
                    │ Title: ${taskDTO.title}
                    │ Description: ${taskDTO.description}
                    │ State ID: ${taskDTO.stateId}
                    │ Created By: ${taskDTO.createdBy}
                    │ Created At: ${taskDTO.createdAt.format()}
                    │ Updated At: ${taskDTO.updatedAt.format()}
                    ╰─────────────────────────────────────────╯
                    """.trimIndent()
                )
            } catch (e: TaskNotFoundException) {
                consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Error retrieving task: ${e.message}\u001B[0m")
            }
        }
    }

    fun addTask() {
        consoleIO.showWithLine("\n\u001B[36m➕ Add New Task\u001B[0m")
        val currentUser = sessionManagerUseCase.getCurrentUser()

        if (currentUser == null) {
            consoleIO.showWithLine("❌ No user is currently logged in.")
            return
        }

        consoleIO.show("Enter Task Title: ")
        val title = consoleIO.read()?.trim()

        consoleIO.show("Enter Task Description: ")
        val description = consoleIO.read()?.trim()

        consoleIO.show("Enter Task State ID: ")
        val stateId = consoleIO.read()?.trim()

        consoleIO.show("Enter Project ID: ")
        val projectId = consoleIO.read()?.trim()

        if (title.isNullOrBlank() || stateId.isNullOrBlank() || projectId.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Title, State ID, and Project ID are required.")
            return
        }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val taskDTO = TaskDto(
            id = Uuid.random().toString(),
            projectId = projectId,
            title = title,
            description = description ?: "",
            createdBy = currentUser.userName,
            stateId = stateId,
            createdAt = now.toString(),
            updatedAt = now.toString()
        )

        showAnimation("Adding task...") {
            try {
                val task = taskDTO.toTaskEntity()
                addTaskUseCase.addTask(task)
                consoleIO.showWithLine("✅ Task added successfully.")

                val actionDetails = "User ${currentUser.userName} created task ${task.id} with title '$title' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = currentUser.role,
                        userName = currentUser.userName,
                        action = Audit.ActionType.CREATE,
                        entityType = Audit.EntityType.TASK,
                        entityId = task.id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )

            } catch (e: TaskAlreadyExistsException) {
                consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to add task: ${e.message}\u001B[0m")
            }
        }
    }

    fun updateTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔄 Update Task\u001B[0m")
        consoleIO.show("Enter Task ID to update: ")
        val id = consoleIO.read()?.trim()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

        showAnimation("Updating task...") {
            try {
                val existingTaskDTO = getTaskByIdUseCase.getTaskById(id)

                consoleIO.show("Enter New Title [${existingTaskDTO.title}]: ")
                val newTitleInput = consoleIO.read()?.trim()
                val newTitle = newTitleInput.takeIf { !it.isNullOrBlank() } ?: existingTaskDTO.title

                consoleIO.show("Enter New Description [${existingTaskDTO.description}]: ")
                val newDescriptionInput = consoleIO.read()?.trim()
                val newDescription = newDescriptionInput.takeIf { !it.isNullOrBlank() } ?: existingTaskDTO.description

                consoleIO.show("Enter New State ID [${existingTaskDTO.stateId}]: ")
                val newStateIdInput = consoleIO.read()?.trim()
                val newStateId = newStateIdInput.takeIf { !it.isNullOrBlank() } ?: existingTaskDTO.stateId

                val updatedTaskDTO = TaskDto(
                    id = existingTaskDTO.id.toString(),
                    projectId = existingTaskDTO.projectId,
                    title = newTitle,
                    description = newDescription,
                    createdBy = existingTaskDTO.createdBy,
                    stateId = newStateId,
                    createdAt = existingTaskDTO.createdAt.toString(),
                    updatedAt = now.toString()
                )

                val updatedTask = updatedTaskDTO.toTaskEntity()
                val resultTaskDTO = updateTaskUseCase.updateTask(updatedTask)

                consoleIO.showWithLine("✅ Task updated successfully:\n📌 Title: ${resultTaskDTO.title}, 📝 Description: ${resultTaskDTO.description}, 🔄 State: ${resultTaskDTO.stateId}")

                sessionManagerUseCase.getCurrentUser()?.let { user ->
                    val actionDetails = "User ${user.userName} updated task $id with title '$newTitle' at ${now.format()}"
                    addAudit.addAuditLog(
                        Audit(
                            id = Uuid.random(),
                            userRole = user.role,
                            userName = user.userName,
                            action = Audit.ActionType.UPDATE,
                            entityType = Audit.EntityType.TASK,
                            entityId = id,
                            actionDetails = actionDetails,
                            timeStamp = now
                        )
                    )
                }

            } catch (e: TaskNotFoundException) {
                consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to update task: ${e.message}\u001B[0m")
            }
        }
    }

    fun deleteTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🗑️ Delete Task\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID to delete: \u001B[0m")
        val id = consoleIO.read()?.trim()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

        showAnimation("Deleting task...") {
            try {
                val taskTitle = getTaskByIdUseCase.getTaskById(id).title
                deleteTaskUseCase.deleteTask(id)
                consoleIO.showWithLine("✅ Task deleted successfully.")

                sessionManagerUseCase.getCurrentUser()?.let { user ->
                    val actionDetails = "User ${user.userName} deleted task $id with title '$taskTitle' at ${now.format()}"
                    addAudit.addAuditLog(
                        Audit(
                            id = Uuid.random(),
                            userRole = user.role,
                            userName = user.userName,
                            action = Audit.ActionType.DELETE,
                            entityType = Audit.EntityType.TASK,
                            entityId = id,
                            actionDetails = actionDetails,
                            timeStamp = now
                        )
                    )
                }

            } catch (e: TaskNotFoundException) {
                consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
            } catch (e: TaskException) {
                consoleIO.showWithLine("\u001B[31m❌ Error deleting task: ${e.message}\u001B[0m")
            }
        }
    }
}