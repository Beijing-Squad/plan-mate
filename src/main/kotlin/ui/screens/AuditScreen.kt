package ui.screens

import logic.entities.Audit
import logic.entities.EntityType
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO

class AuditScreen(
    private val getAllAudits: GetAllAuditLogsUseCase,
    private val getAuditLogsByProjectId: GetAuditLogsByProjectIdUseCase,
    private val getAuditLogsByTaskId: GetAuditLogsByTaskIdUseCase,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {
    override val id: String
        get() = "2"
    override val name: String
        get() = "Audit Screen"

    override fun showOptionService() {
        consoleIO.showWithLine(
            """
        ╔════════════════════════════════════════╗
        ║      Audit Logs Management System      ║
        ╚════════════════════════════════════════╝

        ┌─── Available Options ───────────────────┐
        │                                         │
        │  1. 📋 List All Audit Logs              │
        │  2. 🔍 Find Audit Logs By Project ID    │
        │  3. 🔍️ Find Audit Logs By Task ID       │
        │  0. 🔙 Exit to Main Menu                │
        │                                         │
        └─────────────────────────────────────────┘

        """
                .trimIndent()
        )

        consoleIO.show("Please enter your choice: ")
    }

    override fun handleFeatureChoice() {
        when (getInput()) {
            "1" -> onClickGetAllAuditLogs()
            "2" -> onClickGetAllAuditLogsByProjectID()
            "3" -> onClickGetAllAuditLogsByTaskID()
            "0" -> return
            else -> consoleIO.showWithLine("❌ Invalid Option")
        }
    }

    private fun onClickGetAllAuditLogs() {
        val allAudits = getAllAudits.getAllAuditLogs()
        if (allAudits.isEmpty()) {
            consoleIO.showWithLine("❌ No Audit Logs Found")
        } else {
            consoleIO.showWithLine("\n📋 All Audit Logs:\n")
            allAudits.forEach { audit ->
                consoleIO.showWithLine(formatAuditLog(audit))
            }
        }
    }


    private fun onClickGetAllAuditLogsByProjectID() {
        val projectId = getIdInput()
        val auditLogs = try {
            getAuditLogsByProjectId.getAuditLogsByProjectId(projectId)
        } catch (exception: Exception) {
            consoleIO.showWithLine("❌ ${exception.message}")
            return
        }

        consoleIO.showWithLine("\n🔍 Audit Logs For Project ID: $projectId\n")
        auditLogs.forEach { audit ->
            consoleIO.showWithLine(formatAuditLog(audit))
        }
    }

    private fun onClickGetAllAuditLogsByTaskID() {
        TODO("Not yet implemented")
    }

    private fun formatAuditLog(audit: Audit): String {
        val role = audit.userRole.name.padEnd(7)
        val userName = audit.userName.padEnd(12)
        val action = audit.action.name.padEnd(8)
        val entity = audit.entityType.name.padEnd(8)
        val entityId = audit.entityId.take(13).padEnd(12)
        val date = audit.timeStamp.toString()

        return if (audit.entityType == EntityType.TASK) {
            val oldState = audit.oldState ?: "N/A"
            val newState = audit.newState ?: "N/A"
            "$role| $userName| $action| $entity| $entityId| $oldState| $newState| $date"
        } else {
            "$role| $userName| $action| $entity| $entityId| $date"
        }
    }

    private fun getIdInput(): String {
        consoleIO.show("Enter Project ID: ")
        return consoleIO.read()?.trim() ?: ""
    }

}