package ui.screens

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.TaskState
import logic.exceptions.InvalidStateNameException
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.StateAlreadyExistException
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.state.*
import ui.enums.StateBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskStateScreen(
    private val addTaskStateUseCase: AddTaskStateUseCase,
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val updateTaskStateUseCase: UpdateTaskStateUseCase,
    private val getAllStates: GetAllTaskStatesUseCase,
    private val getStateById: GetTaskStateByIdUseCase,
    private val getStatesByProjectId: GetStatesByProjectIdUseCase,
    private val addAudit: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO,
    private val sessionManagerUseCase: SessionManagerUseCase
) : BaseScreen(consoleIO) {

    override val id = "2"
    override val name = "State Management Screen"

    override fun showOptionService() {
        MenuRenderer.renderMenu(
            """
        ╔══════════════════════════════════════╗
        ║            State Management          ║
        ╚══════════════════════════════════════╝
        """,
            StateBoardOption.entries,
            consoleIO
        )
    }

    override fun handleFeatureChoice() {
        while (true) {
            when (getInput()) {
                "1" -> onChooseAddState()
                "2" -> onChooseDeleteState()
                "3" -> onChooseUpdateState()
                "4" -> onChooseGetAllStates()
                "5" -> onChooseGetStateById()
                "6" -> onChooseGetStatesByProjectId()
                "0" -> return
                else -> {
                    consoleIO.showWithLine("❌ Invalid Option")
                }
            }
            showOptionService()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onChooseAddState() {
        runBlocking {
            try {
                val name = getInputWithLabel("📛 Enter State Name: ")
                val projectId = Uuid.parse(getInputWithLabel("📁 Enter Project ID: "))
                val taskState = TaskState(name = name, projectId = projectId)
                val result = addTaskStateUseCase.addState(taskState)
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

//                sessionManagerUseCase.getCurrentUser()?.userName?.let { userName ->
//                    val actionDetails =
//                        "Admin $userName added new state ${taskState.id} with name '$name' at ${now.format()}"
//
//                    addAudit.addAuditLog(
//                        Audit(
//                            id = Uuid.random(),
//                            userRole = UserRole.ADMIN,
//                            userName = sessionManagerUseCase.getCurrentUser()!!.userName,
//                            action = ActionType.UPDATE,
//                            entityType = EntityType.PROJECT,
//                            entityId = projectId.toString(),
//                            actionDetails = actionDetails,
//                            timeStamp = now
//                        )
//                    )
//                }

                showResult(result, "added")
            } catch (e: StateAlreadyExistException) {
                consoleIO.showWithLine("⚠️ ${e.message}")
            } catch (e: InvalidStateNameException) {
                consoleIO.showWithLine("⚠️ ${e.message}")
            } catch (e: ProjectNotFoundException) {
                consoleIO.showWithLine("⚠️ ${e.message}")
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ Unexpected error: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onChooseDeleteState() {
        runBlocking {
            try {
                val id = Uuid.parse(getInputWithLabel("🆔 Enter State ID to delete: "))
//                val taskState = TaskState(id = id, name = "", projectId = Uuid.parse(""))
                val result = deleteTaskStateUseCase.deleteState(id)
                showResult(result, "deleted")
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onChooseUpdateState() {
        runBlocking {
            try {
                val id = Uuid.parse(getInputWithLabel("🆔 Enter State ID to update: "))
                val name = getInputWithLabel("📛 Enter New State Name: ")
                val projectId = Uuid.parse(getInputWithLabel("📁 Enter New Project ID: "))
                val taskState = TaskState(id = id, name = name, projectId = projectId)
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val updated = updateTaskStateUseCase.updateState(taskState)
//                sessionManagerUseCase.getCurrentUser()?.userName?.let { userName ->
//                    val actionDetails =
//                        "Admin $userName updated state ${taskState.id} with name '$name' at ${now.format()}"
//                    addAudit.addAuditLog(
//                        Audit(
//                            id = Uuid.random(),
//                            userRole = UserRole.ADMIN,
//                            userName = sessionManagerUseCase.getCurrentUser()!!.userName,
//                            action = ActionType.UPDATE,
//                            entityType = EntityType.PROJECT,
//                            entityId = projectId.toString(),
//                            actionDetails = actionDetails,
//                            timeStamp = now
//                        )
//                    )
//                }
                consoleIO.showWithLine("✅ State updated:\n${formatState(updated)}")
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ ${e.message}")
            }
        }
    }

    private fun onChooseGetAllStates() {
        runBlocking {
            try {
                val states = getAllStates.getAllStates()
                if (states.isEmpty()) {
                    consoleIO.showWithLine("❌ No states found")
                } else {
                    consoleIO.showWithLine("\n📋 All States:\n")
                    states.forEach { consoleIO.showWithLine(formatState(it)) }
                }
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ ${e.message}")
            }
        }
    }

    private fun onChooseGetStateById() {
        runBlocking {
            try {
                val id = getInputWithLabel("🆔 Enter State ID: ")
                val state = getStateById.getStateById(id)
                consoleIO.showWithLine("✅ State found:\n${formatState(state)}")
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ ${e.message}")
            }
        }
    }

    private fun onChooseGetStatesByProjectId() {
        runBlocking {
            try {
                val projectId = getInputWithLabel("📁 Enter Project ID: ")
                val states = getStatesByProjectId.getStatesByProjectId(projectId)
                if (states.isEmpty()) {
                    consoleIO.showWithLine("❌ No states found for this project.")
                } else {
                    consoleIO.showWithLine("\n📁 States in project:\n")
                    states.forEach { consoleIO.showWithLine(formatState(it)) }
                }
            } catch (e: Exception) {
                consoleIO.showWithLine("❌ ${e.message}")
            }
        }
    }

    private fun getInputWithLabel(label: String): String {
        consoleIO.show(label)
        return consoleIO.read()?.trim().orEmpty()
    }

    private fun showResult(result: Boolean, action: String) {
        if (result) {
            consoleIO.showWithLine("✅ State $action successfully!")
        } else {
            consoleIO.showWithLine("❌ Failed to $action state.")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun formatState(taskState: TaskState): String {
        return """
            ╔═══════════════════════════════════════════════════════╗
            ║ 🆔 State ID  : ${taskState.id.toString().padEnd(31)}   ║
            ║ 📛 Name      : ${taskState.name.padEnd(39)}║
            ║ 🗂️ Project ID: ${taskState.projectId.toString().padEnd(39)}║
            ╚═══════════════════════════════════════════════════════╝
        """.trimIndent()
    }
}