package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository

class AddAuditLogUseCase(
    private val auditRepository: AuditRepository
) {

    fun addAuditLog(audit: Audit) {
        auditRepository.addAuditLog(audit)
    }
}