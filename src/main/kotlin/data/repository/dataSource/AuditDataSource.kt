package data.repository.dataSource

import logic.entities.Audit

interface AuditDataSource {

     fun getAllAuditLogs(): List<Audit>

     fun addAuditLog(audit: Audit)

     fun getAuditLogsByProjectId(projectId: String): List<Audit>

     fun getAuditLogsByTaskId(taskId: String): List<Audit>
}