package data.dataSource.csv

import data.repository.dataSourceAbstraction.AuditDataSource
import logic.entities.Audit
import java.io.File

class AuditCsvDataSource(
    private val file:File
):AuditDataSource{

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