package data.csvDataSource

import data.parser.CsvPlanMateParser
import data.parser.CsvPlanMateReader
import data.repository.dataSource.AuditDataSource
import logic.entities.Audit

class AuditCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
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