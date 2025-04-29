package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository

class GetAuditLogsByTaskIdUseCase(
    private val auditRepository: AuditRepository
) {

    fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return emptyList()
    }
}