package logic.useCases.audit

import logic.repository.AuditRepository

class AddAuditLogUseCase(
    private val auditRepository: AuditRepository
) {
}