package data.csvDataSource


import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.AuditDataSource
import logic.entities.Audit
import logic.entities.EntityType

class AuditCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Audit>
) : AuditDataSource {

    override fun getAllAuditLogs(): List<Audit> {
        return csvDataSource.loadAllDataFromFile()
    }

    override fun addAuditLog(audit: Audit) {
        csvDataSource.appendToFile(audit)
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return getAllAuditLogs().filter { auditLog -> isMatchingProject(auditLog, projectId) }
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return getAllAuditLogs().filter { audit: Audit ->
            audit.entityId == taskId
                    && audit.entityType == EntityType.TASK
        }
    }

    private fun isMatchingProject(audit: Audit, projectId: String): Boolean {
        return audit.entityId == projectId && audit.entityType == EntityType.PROJECT
    }
}