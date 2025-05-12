
import logic.entities.Audit
import logic.repository.AuditRepository

class GetAuditLogsByProjectIdUseCase(
    private val auditRepository: AuditRepository
) {
    suspend fun getAuditLogsByProjectId(projectId: String): List<Audit>? {

        return auditRepository.getAuditLogsByProjectId(projectId)
            .takeIf { it.isNotEmpty() }
            ?.sortedByDescending { it.timeStamp }
    }
}

private fun String?.isValidProjectId(): Boolean {
    return !this.isNullOrBlank()
}
