package logic.useCases.audit

import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.exceptions.InvalidInputException
import logic.entities.exceptions.ProjectNotFoundException
import logic.repository.AuditRepository

class GetAuditLogsByProjectIdUseCase(
    private val auditRepository: AuditRepository
) {
    fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        validateProjectId(projectId)
        return auditRepository.getAllAuditLogs()
            .filter { auditLog -> isMatchingProject(auditLog, projectId) }
            .ifEmpty { throw ProjectNotFoundException(PROJECT_NOT_FOUND_ERROR) }
            .sortedByDescending { auditLog -> auditLog.timeStamp }
    }

    private fun validateProjectId(projectId: String) {
        if (projectId.isBlank()) throw InvalidInputException(INVALID_ID_ERROR)
    }

    private fun isMatchingProject(audit: Audit, projectId: String): Boolean {
        return audit.entityId == projectId && audit.entityType == EntityType.PROJECT
    }

    companion object {
        private const val INVALID_ID_ERROR = "Error: ID shouldn't be blank"
        private const val PROJECT_NOT_FOUND_ERROR = "Error: Project logs not found"
    }
}