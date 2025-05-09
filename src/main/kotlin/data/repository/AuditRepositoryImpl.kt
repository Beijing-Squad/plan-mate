package data.repository

import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.repository.mapper.toAuditDTO
import data.repository.mapper.toAuditEntity
import logic.entities.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val auditDataSource: MongoDBDataSourceImpl
) : AuditRepository {

    override suspend fun getAllAuditLogs(): List<Audit> {
        return auditDataSource.getAllAuditLogs().map { toAuditEntity(it) }
    }

    override suspend fun addAuditLog(audit: Audit) {
        auditDataSource.addAuditLog(toAuditDTO(audit))
    }

    override suspend fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return auditDataSource.getAuditLogsByProjectId(projectId).map { toAuditEntity(it) }
    }

    override suspend fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return auditDataSource.getAuditLogsByTaskId(taskId).map { toAuditEntity(it) }
    }
}
