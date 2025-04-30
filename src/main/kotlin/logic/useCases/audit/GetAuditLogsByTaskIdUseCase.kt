package logic.useCases.audit

import logic.entities.Audit
import logic.entities.exceptions.InvalidInputException
import logic.repository.AuditRepository

class GetAuditLogsByTaskIdUseCase(
    private val auditRepository: AuditRepository
) {

    fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        validateTaskId(taskId)
        return auditRepository.getAuditLogsByTaskId(taskId)
    }

    private fun validateTaskId(taskId: String) {
        if (taskId.isBlank()) throw InvalidInputException(INVALID_ID_ERROR)
    }

    companion object {
        private const val INVALID_ID_ERROR = "Error: ID shouldn't be blank"
    }
}