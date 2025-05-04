package ui.screens

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import logic.entities.*
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManager
import logic.useCases.state.*
import ui.enums.StateBoardOption
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO
import ui.main.MenuRenderer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StateScreen(
    private val addStateUseCase: AddStateUseCase,
    private val deleteStateUseCase: DeleteStateUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    private val getAllStates: GetAllStatesUseCase,
    private val getStateById: GetStateByIdUseCase,
    private val getStatesByProjectId: GetStatesByProjectIdUseCase,
    private val addAudit: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO,
    private val sessionManager: SessionManager
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
        when (getInput()) {
            "1" -> onChooseAddState()
            "2" -> onChooseDeleteState()
            "3" -> onChooseUpdateState()
            "4" -> onChooseGetAllStates()
            "5" -> onChooseGetStateById()
            "6" -> onChooseGetStatesByProjectId()
            "0" -> return
            else -> consoleIO.showWithLine("❌ Invalid Option")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onChooseAddState() {
        try {
            val id = getInputWithLabel("🆔 Enter State ID: ")
            val name = getInputWithLabel("📛 Enter State Name: ")
            val projectId = getInputWithLabel("📁 Enter Project ID: ")
            val role = getRoleInput()

            val state = State(id = id, name = name, projectId = projectId)

            val result = addStateUseCase.addState(state, role)
            addAudit.addAuditLog(
                Audit(
                    id = Uuid.random(),
                    userRole = role,
                    userName = sessionManager.getCurrentUser()!!.userName,
                    action = ActionType.UPDATE,
                    entityType = EntityType.PROJECT,
                    entityId = projectId,
                    oldState = "",
                    newState = name,
                    timeStamp = Clock.System.todayIn(TimeZone.currentSystemDefault())
                )
            )
            showResult(result, "added")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    private fun onChooseDeleteState() {
        try {
            val id = getInputWithLabel("🆔 Enter State ID to delete: ")
            val role = getRoleInput()

            val state = State(id = id, name = "", projectId = "")
            val result = deleteStateUseCase.deleteState(state, role)
            showResult(result, "deleted")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onChooseUpdateState() {
        try {
            val id = getInputWithLabel("🆔 Enter State ID to update: ")
            val name = getInputWithLabel("📛 Enter New State Name: ")
            val projectId = getInputWithLabel("📁 Enter New Project ID: ")
            val role = getRoleInput()

            val state = State(id = id, name = name, projectId = projectId)
            val updated = updateStateUseCase.updateState(state, role)
            addAudit.addAuditLog(
                Audit(
                    id = Uuid.random(),
                    userRole = role,
                    userName = sessionManager.getCurrentUser()!!.userName,
                    action = ActionType.UPDATE,
                    entityType = EntityType.PROJECT,
                    entityId = projectId,
                    oldState = "",
                    newState = name,
                    timeStamp = Clock.System.todayIn(TimeZone.currentSystemDefault())
                )
            )
            consoleIO.showWithLine("✅ State updated:\n$updated")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    private fun onChooseGetAllStates() {
        try {
            val states = getAllStates.getAllStates()
            if (states.isEmpty()) {
                consoleIO.showWithLine("❌ No states found")
            } else {
                consoleIO.showWithLine("\n📋 All States:\n")
                states.forEach { consoleIO.showWithLine(it.toString()) }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    private fun onChooseGetStateById() {
        try {
            val id = getInputWithLabel("🆔 Enter State ID: ")
            val state = getStateById.getStateById(id)
            consoleIO.showWithLine("✅ State found:\n$state")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    private fun onChooseGetStatesByProjectId() {
        try {
            val projectId = getInputWithLabel("📁 Enter Project ID: ")
            val states = getStatesByProjectId.getStatesByProjectId(projectId)
            consoleIO.showWithLine("\n📁 States in project:\n")
            states.forEach { consoleIO.showWithLine(it.toString()) }
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
        }
    }

    private fun getInputWithLabel(label: String): String {
        consoleIO.show(label)
        return consoleIO.read()?.trim().orEmpty()
    }

    private fun getRoleInput(): UserRole {
        return sessionManager.getCurrentUser()?.role ?: UserRole.MATE
    }


    private fun showResult(result: Boolean, action: String) {
        if (result) {
            consoleIO.showWithLine("✅ State $action successfully!")
        } else {
            consoleIO.showWithLine("❌ Failed to $action state.")
        }
    }
}