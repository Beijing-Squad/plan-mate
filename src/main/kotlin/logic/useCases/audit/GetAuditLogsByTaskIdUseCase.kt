package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetAuditLogsByTaskIdUseCase(
    private val auditRepository: AuditRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun getAuditLogsByTaskId(taskId: Uuid): List<Audit> {
        return emptyList()
    }
}