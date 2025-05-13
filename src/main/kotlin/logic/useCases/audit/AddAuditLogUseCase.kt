package logic.useCases.audit

import logic.entities.Audit
import logic.repository.AuditRepository
import logic.useCases.authentication.SessionManagerUseCase
import kotlin.uuid.ExperimentalUuidApi

class AddAuditLogUseCase(
    private val auditRepository: AuditRepository,
    private val sessionManagerUseCase: SessionManagerUseCase
) {

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addAuditLog(audit: Audit) {
        sessionManagerUseCase.getCurrentUser()?.let { audit.copy(userRole = it.role, userName = it.userName) }
            ?.let { auditRepository.addAuditLog(it) }
    }
}