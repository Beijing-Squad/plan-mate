package logic.useCases.audit

import logic.repository.AuditRepository

class GetAuditLogsByTaskIdUseCase(
    private val auditRepository: AuditRepository
) {
}