package ui.screens

import logic.entities.State
import logic.entities.UserRole
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.useCases.state.*
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

class StateScreen(
    private val addStateUseCase: AddStateUseCase,
    private val deleteStateUseCase: DeleteStateUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    private val getAllStates: GetAllStatesUseCase,
    private val getStateById: GetStateByIdUseCase,
    private val getStatesByProjectId: GetStatesByProjectIdUseCase,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {

    override val id = "1"
    override val name = "State Management Screen"

    override fun showOptionService() {
        consoleIO.showWithLine(
            """
            ╔══════════════════════════════════════╗
            ║         📂 State Management          ║
            ╚══════════════════════════════════════╝

            ┌─── Available Options ───────────────┐
            │                                     │
            │  1. ➕ Add State                    │
            │  2. ❌ Delete State                 │
            │  3. 🔁 Update State                 │
            │  4. 📋 Get All States               │
            │  5. 🔍 Get State by ID              │
            │  6. 📁 Get States by Project ID     │
            │  0. 🔙 Back to Main Menu            │
            │                                     │
            └─────────────────────────────────────┘
            """.trimIndent()
        )
        consoleIO.show("Please enter your choice: ")
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

    private fun onChooseUpdateState() {
        try {
            val id = getInputWithLabel("🆔 Enter State ID to update: ")
            val name = getInputWithLabel("📛 Enter New State Name: ")
            val projectId = getInputWithLabel("📁 Enter New Project ID: ")
            val role = getRoleInput()

            val state = State(id = id, name = name, projectId = projectId)
            val updated = updateStateUseCase.updateState(state, role)

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
        val roleInput = getInputWithLabel("👤 Enter your role (ADMIN / MATE): ")
        return try {
            UserRole.valueOf(roleInput.uppercase())
        } catch (e: Exception) {
            throw StateUnauthorizedUserException("❌ Invalid role")
        }
    }

    /*
    private fun getRoleInput(): UserRole {
        return SessionManager.getCurrentUser?.role
    }
     */

    private fun showResult(result: Boolean, action: String) {
        if (result) {
            consoleIO.showWithLine("✅ State $action successfully!")
        } else {
            consoleIO.showWithLine("❌ Failed to $action state.")
        }
    }
}