package ui.screens

import logic.entity.Audit
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import ui.enums.AuditBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO

class AuditScreen(
    private val getAllAudits: GetAllAuditLogsUseCase,
    private val getAuditLogsByProjectId: GetAuditLogsByProjectIdUseCase,
    private val getAuditLogsByTaskId: GetAuditLogsByTaskIdUseCase,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {

    override val id: String
        get() = "4"
    override val name: String
        get() = "Audit Screen"

    override fun showOptionService() {
        MenuRenderer.renderMenu(
            """
        ╔════════════════════════════════════════╗
        ║       Audit Logs Management System     ║
        ╚════════════════════════════════════════╝
        """.trimIndent(), AuditBoardOption.entries, consoleIO
        )
    }

    override fun handleFeatureChoice() {
        while (true) {
            when (getInput()) {
                "1" -> onClickGetAllAuditLogs()
                "2" -> onClickGetAuditLogsForProject()
                "3" -> onClickGetAuditLogsForTask()
                "0" -> return
                else -> consoleIO.showWithLine("❌ Invalid Option")
            }
            showOptionService()
        }
    }

    private fun onClickGetAllAuditLogs() {
        showAnimation("Fetching all audit logs...") {
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
    }


    private fun onClickGetAuditLogsForProject() {
        val projectId = getIdInput()
        if (projectId.isBlank()) {
            consoleIO.showWithLine("❌ Error: ID shouldn't be blank")
            return
        }
        showAnimation("Fetching project audit logs...") {
            val auditLogs = getAuditLogsByProjectId.getAuditLogsByProjectId(projectId)

            if (auditLogs.isNullOrEmpty()) {
                consoleIO.showWithLine("❌ No Audit Logs Found")
                return@showAnimation
            }

            consoleIO.showWithLine("\n🔍 Audit Logs For Project ID: $projectId\n")
            auditLogs.forEach { audit ->
                consoleIO.showWithLine(formatAuditLog(audit))
            }
        }
    }


    private fun onClickGetAuditLogsForTask() {
        val taskId = getIdInput()

        if (taskId.isBlank()) {
            consoleIO.showWithLine("❌ Error: ID shouldn't be blank")
            return
        }
        showAnimation("Fetching task audit logs...") {
            val auditLogs = getAuditLogsByTaskId.getAuditLogsByTaskId(taskId)

            if (auditLogs.isEmpty()) {
                consoleIO.showWithLine("❌ No Audit Logs Found")
                return@showAnimation
            }

            consoleIO.showWithLine("\n🔍 Audit Logs For Task ID: $taskId\n")
            auditLogs.forEach { audit ->
                consoleIO.showWithLine(formatAuditLog(audit))
            }
        }
    }


    private fun formatAuditLog(audit: Audit): String {
        val role = audit.userRole.name.padEnd(7)
        val userName = audit.userName.padEnd(12)
        val action = audit.action.name.padEnd(8)
        val entity = audit.entityType.name.padEnd(8)
        val entityId = audit.entityId.take(13).padEnd(12)
        val actionDetails = (audit.actionDetails ?: "").padEnd(17)
        val date = audit.timeStamp.toString()

        return "$role| $userName| $action| $entity| $entityId| $actionDetails| $date"
    }

    private fun getIdInput(): String {
        consoleIO.show("Enter ID: ")
        return consoleIO.read()?.trim() ?: ""
    }
}
