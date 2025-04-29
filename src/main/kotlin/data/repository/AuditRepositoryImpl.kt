package data.repository

import data.repository.dataSource.AuditDataSource
import logic.entities.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl (
    private val auditDataSource: AuditDataSource
): AuditRepository{
    override fun getAllAuditLogs(): List<Audit> {
        return auditDataSource.getAllAuditLogs()
    }

    override fun addAuditLog(audit: Audit) {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        TODO("Not yet implemented")
    }

}