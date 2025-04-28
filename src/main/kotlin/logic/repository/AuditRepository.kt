package logic.repository

import logic.entities.Audit

interface AuditRepository{
    fun getAllAuditLogs(): List<Audit>

    fun addAuditLog(audit: Audit)

    fun getAuditLogsByProjectId(projectId: String): List<Audit>

    fun getAuditLogsByTaskId(taskId: String): List<Audit>

}