package ui.serviceImpl

import logic.entities.Audit
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import ui.service.AuditUIService
import ui.service.ConsoleIOService

class AuditUIServiceImpl(
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase,
    private val getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase,
    private val getAllAuditLogsUseCase: GetAllAuditLogsUseCase,
    private val console: ConsoleIOService
): AuditUIService {
    override fun getAllAuditLogs(): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun addAuditLog(audit: Audit) {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        TODO("Not yet implemented")
    }

}
