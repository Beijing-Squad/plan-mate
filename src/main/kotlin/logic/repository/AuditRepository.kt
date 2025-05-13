package logic.repository

import logic.entity.Audit

interface AuditRepository{
    suspend fun getAllAuditLogs(): List<Audit>

    suspend fun addAuditLog(audit: Audit)

    suspend fun getAuditLogsByProjectId(projectId: String): List<Audit>

    suspend fun getAuditLogsByTaskId(taskId: String): List<Audit>

}