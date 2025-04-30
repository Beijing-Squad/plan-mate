package data.csvDataSource


import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.AuditDataSource
import logic.entities.Audit

class AuditCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<Audit>
): AuditDataSource {

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
        TODO("Not yet implemented")
    }

}