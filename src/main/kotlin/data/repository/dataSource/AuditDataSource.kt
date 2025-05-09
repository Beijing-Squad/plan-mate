package data.repository.dataSource

import data.dto.AuditDTO
import logic.entities.Audit

interface AuditDataSource {

    suspend fun getAllAuditLogs(): List<Audit>

    suspend fun addAuditLog(audit: AuditDTO)

    suspend fun getAuditLogsByProjectId(projectId: String): List<Audit>

    suspend fun getAuditLogsByTaskId(taskId: String): List<Audit>
}