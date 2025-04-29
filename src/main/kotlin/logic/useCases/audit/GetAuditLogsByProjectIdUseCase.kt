package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository

class GetAuditLogsByProjectIdUseCase(
    private val auditRepository: AuditRepository
) {
    fun getAuditLogsByProjectId(projectId: String): List<Audit>{
        return emptyList()
    }
}