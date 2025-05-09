package ui.screens

import com.mongodb.client.model.Filters
import kotlinx.coroutines.runBlocking
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
import data.dto.TaskDTO
import data.repository.mapper.toTaskDTO
import data.repository.mapper.toTaskEntity
import format
import logic.entities.exceptions.TaskNotFoundException
import org.bson.Document
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDateTime

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
    private val database: MongoDatabase = MongoConnection.database
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
                "1" -> runBlocking { showTasksInSwimlanes() }
                "2" -> runBlocking { addTask() }
                "3" -> runBlocking { getTaskById() }
                "4" -> runBlocking { deleteTaskById() }
                "5" -> runBlocking { showAllTasksList() }
                "6" -> runBlocking { updateTaskById() }
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option\u001B[0m")
            }
            showOptionService()
        }
    }

    private suspend fun showTasksInSwimlanes() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (Swimlanes View):\u001B[0m")
        try {
            val tasks = getAllTasksUseCase.getAllTasks()
            val states = getAllTaskStatesUseCase.getAllStates()
            swimlanesRenderer.render(tasks, states)
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks: ${e.message}\u001B[0m")
            try {
                val rawDocs = database.getCollection<Document>("tasks").find().toList()
                if (rawDocs.isEmpty()) {
                    consoleIO.showWithLine("⚠️ No tasks available.")
                    return
                }
                val tasks = rawDocs.mapNotNull { doc -> mapRawDocumentToTask(doc) }
                val states = getAllTaskStatesUseCase.getAllStates()
                swimlanesRenderer.render(tasks, states)
                consoleIO.showWithLine("\u001B[33m⚠️ Loaded tasks using fallback method due to schema issues.\u001B[0m")
            } catch (e: Exception) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks even with fallback: ${e.message}\u001B[0m")
            }
        }
    }

    private suspend fun showAllTasksList() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Tasks (List View):\u001B[0m")
        try {
            val tasks = getAllTasksUseCase.getAllTasks()
            if (tasks.isEmpty()) {
                consoleIO.showWithLine("⚠️ No tasks available.")
                return
            }
            tasks.forEach { task ->
                val taskDTO = toTaskDTO(task)
                consoleIO.showWithLine(
                    """
                    ╭────────────────────────────────────────╮
                    │ ID: ${taskDTO.id}
                    │ Title: ${taskDTO.title}
                    │ Description: ${taskDTO.description}
                    │ State ID: ${taskDTO.stateId}
                    │ Created By: ${taskDTO.createdBy}
                    │ Created At: ${taskDTO.createdAt.format()}
                    │ Updated At: ${taskDTO.updatedAt.format()}
                    ╰────────────────────────────────────────╯
                    """.trimIndent()
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks: ${e.message}\u001B[0m")
            try {
                val rawDocs = database.getCollection<Document>("tasks").find().toList()
                if (rawDocs.isEmpty()) {
                    consoleIO.showWithLine("⚠️ No tasks available.")
                    return
                }
                rawDocs.forEach { doc ->
                    val task = mapRawDocumentToTask(doc)
                    if (task != null) {
                        val taskDTO = toTaskDTO(task)
                        consoleIO.showWithLine(
                            """
                            ╭────────────────────────────────────────╮
                            │ ID: ${taskDTO.id}
                            │ Title: ${taskDTO.title}
                            │ Description: ${taskDTO.description}
                            │ State ID: ${taskDTO.stateId}
                            │ Created By: ${taskDTO.createdBy}
                            │ Created At: ${taskDTO.createdAt.format()}
                            │ Updated At: ${taskDTO.updatedAt.format()}
                            ╰────────────────────────────────────────╯
                            """.trimIndent()
                        )
                    }
                }
                consoleIO.showWithLine("\u001B[33m⚠️ Loaded tasks using fallback method due to schema issues.\u001B[0m")
            } catch (e: Exception) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to load tasks even with fallback: ${e.message}\u001B[0m")
            }
        }
    }

    private suspend fun getTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find Task by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID: \u001B[0m")
        val id = consoleIO.read()?.trim()

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

        try {
            val taskDTO = getTaskByIdUseCase.getTaskById(id)
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
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Error retrieving task: ${e.message}\u001B[0m")
            // Fallback to raw document query
            try {
                val rawDoc = database.getCollection<Document>("tasks")
                    .find(Filters.eq("_id", Uuid.parse(id))).limit(1).firstOrNull()
                if (rawDoc == null) {
                    consoleIO.showWithLine("\u001B[31m❌ Task with id $id not found\u001B[0m")
                    return
                }
                val task = mapRawDocumentToTask(rawDoc)
                if (task != null) {
                    val taskDTO = toTaskDTO(task)
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
                    consoleIO.showWithLine("\u001B[33m⚠️ Loaded task using fallback method due to schema issues.\u001B[0m")
                } else {
                    consoleIO.showWithLine("\u001B[31m❌ Failed to parse task data\u001B[0m")
                }
            } catch (e: Exception) {
                consoleIO.showWithLine("\u001B[31m❌ Failed to load task even with fallback: ${e.message}\u001B[0m")
            }
        }
    }

    private suspend fun addTask() {
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

        val taskDTO = TaskDTO(
            id = Uuid.random().toString(),
            projectId = projectId,
            title = title,
            description = description ?: "",
            createdBy = currentUser.userName,
            stateId = stateId,
            createdAt = now.toString(),
            updatedAt = now.toString()
        )

        try {
            val task = toTaskEntity(taskDTO)
            addTaskUseCase.addTask(task)
            consoleIO.showWithLine("✅ Task added successfully.")
            val actionDetails = "User ${currentUser.userName} created task ${task.id} with title '$title' at ${now.format()}"
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
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Failed to add task: ${e.message}\u001B[0m")
        }
    }

    private suspend fun updateTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🔄 Update Task\u001B[0m")
        consoleIO.show("Enter Task ID to update: ")
        val id = consoleIO.read()?.trim()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

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

            val updatedTaskDTO = TaskDTO(
                id = existingTaskDTO.id.toString(),
                projectId = existingTaskDTO.projectId,
                title = newTitle,
                description = newDescription,
                createdBy = existingTaskDTO.createdBy,
                stateId = newStateId,
                createdAt = existingTaskDTO.createdAt.toString(),
                updatedAt = now.toString()
            )

            val updatedTask = toTaskEntity(updatedTaskDTO)
            val resultTaskDTO = updateTaskUseCase.updateTask(updatedTask)

            consoleIO.showWithLine("✅ Task updated successfully:\n📌 Title: ${resultTaskDTO.title}, 📝 Description: ${resultTaskDTO.description}, 🔄 State: ${resultTaskDTO.stateId}")
            sessionManagerUseCase.getCurrentUser()?.let { user ->
                val actionDetails = "User ${user.userName} updated task $id with title '$newTitle' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = user.role,
                        userName = user.userName,
                        action = ActionType.UPDATE,
                        entityType = EntityType.TASK,
                        entityId = id,
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: TaskNotFoundException) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Failed to update task: ${e.message}\u001B[0m")
        }
    }

    private suspend fun deleteTaskById() {
        consoleIO.showWithLine("\n\u001B[36m🗑️ Delete Task\u001B[0m")
        consoleIO.show("\u001B[32mEnter Task ID to delete: \u001B[0m")
        val id = consoleIO.read()?.trim()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (id.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Task ID is required.")
            return
        }

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
                        action = ActionType.DELETE,
                        entityType = EntityType.TASK,
                        entityId = id,
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: TaskNotFoundException) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Error deleting task: ${e.message}\u001B[0m")
        }
    }

    private fun mapRawDocumentToTask(doc: Document): Task? {
        return try {
            Task(
                id = Uuid.parse(doc.getString("_id") ?: return null),
                projectId = doc.getString("project_id") ?: doc.getString("projectId") ?: "",
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                createdBy = doc.getString("created_by") ?: doc.getString("createdBy") ?: "",
                stateId = doc.getString("state_id") ?: doc.getString("stateId") ?: "",
                createdAt = try {
                    val dateStr = doc.getString("created_at") ?: doc.getString("createdAt")
                    if (dateStr != null) LocalDateTime.parse(dateStr) else LocalDateTime.parse("2025-05-09T00:00:00")
                } catch (e: Exception) {
                    LocalDateTime.parse("2025-05-09T00:00:00")
                },
                updatedAt = try {
                    val dateStr = doc.getString("updated_at") ?: doc.getString("updatedAt")
                    if (dateStr != null) LocalDateTime.parse(dateStr) else LocalDateTime.parse("2025-05-09T00:00:00")
                } catch (e: Exception) {
                    LocalDateTime.parse("2025-05-09T00:00:00")
                }
            )
        } catch (e: Exception) {
            null
        }
    }
}