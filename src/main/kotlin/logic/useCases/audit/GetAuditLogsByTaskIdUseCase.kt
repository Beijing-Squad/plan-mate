package logic.useCases.audit

import logic.entities.Audit
import logic.entities.exceptions.InvalidInputException
import logic.repository.AuditRepository

class GetAuditLogsByTaskIdUseCase(
    private val auditRepository: AuditRepository
) {

    suspend fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        if (taskId.isBlank()) throw InvalidInputException(INVALID_ID_ERROR)

        return auditRepository.getAuditLogsByTaskId(taskId)
            .sortedByDescending { it.timeStamp }
    }

    companion object {
        private const val INVALID_ID_ERROR = "Error: ID shouldn't be blank"
    }

}