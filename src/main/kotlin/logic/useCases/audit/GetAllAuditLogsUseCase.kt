package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository

class GetAllAuditLogsUseCase(
    private val auditRepository: AuditRepository
) {

    fun getAllAuditLogs(): List<Audit> {
        return emptyList()
    }
}