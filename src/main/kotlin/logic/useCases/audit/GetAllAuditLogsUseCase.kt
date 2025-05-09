package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository

class GetAllAuditLogsUseCase(
    private val auditRepository: AuditRepository
) {

    suspend fun getAllAuditLogs(): List<Audit> {
        return auditRepository.getAllAuditLogs()
            .sortedByDescending { auditLog -> auditLog.timeStamp }
    }
}