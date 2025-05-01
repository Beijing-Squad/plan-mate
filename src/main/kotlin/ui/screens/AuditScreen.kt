package ui.screens

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
        TODO("Not yet implemented")
    }

    private fun onClickGetAllAuditLogsByProjectID() {
        TODO("Not yet implemented")
    }

    private fun onClickGetAllAuditLogsByTaskID() {
        TODO("Not yet implemented")
    }

}