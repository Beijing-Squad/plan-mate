package data.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.AuditDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.Audit
import logic.entities.Project

class AuditMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : AuditDataSource {
    private val collection = database.getCollection<Project>("audits")
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