package logic.useCases.audit

import logic.repository.AuditRepository

class GetAuditLogsByProjectIdUseCase(
    private val auditRepository: AuditRepository
) {
}