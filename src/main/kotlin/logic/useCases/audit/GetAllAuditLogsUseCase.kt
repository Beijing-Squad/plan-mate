package logic.useCases.audit

import logic.repository.AuditRepository

class GetAllAuditLogsUseCase(
    private val auditRepository: AuditRepository
) {

}