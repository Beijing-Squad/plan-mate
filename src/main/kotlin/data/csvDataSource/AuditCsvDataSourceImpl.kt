package data.csvDataSource


import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.AuditDataSource
import logic.entities.Audit
import logic.entities.EntityType

class AuditCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Audit>
) : AuditDataSource {

    override fun getAllAuditLogs(): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun addAuditLog(audit: Audit) {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        TODO("Not yet implemented")
    }

    override fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return csvDataSource.loadAllDataFromFile()
            .filter { audit: Audit ->
                audit.entityId == taskId
                        && audit.entityType == EntityType.TASK
            }
    }

}