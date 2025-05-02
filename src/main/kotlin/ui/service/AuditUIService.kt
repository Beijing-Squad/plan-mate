package ui.service

import logic.entities.Audit

interface AuditUIService {
    fun getAllAuditLogs(): List<Audit>

    fun addAuditLog(audit: Audit)

    fun getAuditLogsByProjectId(projectId: String): List<Audit>

    fun getAuditLogsByTaskId(taskId: String): List<Audit>

}