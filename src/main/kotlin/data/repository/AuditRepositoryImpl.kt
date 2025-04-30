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
        auditDataSource.addAuditLog(audit)
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return auditDataSource.getAuditLogsByProjectId(projectId)
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return auditDataSource.getAuditLogsByTaskId(taskId)

    }

}