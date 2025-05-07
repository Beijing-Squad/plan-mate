package logic.useCases.audit

import logic.entities.Audit
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.ProjectNotFoundException
import logic.repository.AuditRepository

class GetAuditLogsByProjectIdUseCase(
    private val auditRepository: AuditRepository
) {
    fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        if (projectId.isBlank()) throw InvalidInputException(INVALID_ID_ERROR)

        return auditRepository.getAuditLogsByProjectId(projectId)
            .orThrowIfEmpty(PROJECT_NOT_FOUND_ERROR)
            .sortedByDescending { it.timeStamp }
    }

    private fun <T> Collection<T>.orThrowIfEmpty(errorMessage: String): Collection<T> {
        if (isEmpty()) throw ProjectNotFoundException(errorMessage)
        return this
    }

    companion object {
        private const val INVALID_ID_ERROR = "Error: ID shouldn't be blank"
        private const val PROJECT_NOT_FOUND_ERROR = "Error: Project logs not found"
    }
}