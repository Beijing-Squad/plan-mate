package data.repository

import data.repository.mapper.toAuditDto
import data.repository.mapper.toAuditEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entity.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : AuditRepository {

    override suspend fun getAllAuditLogs(): List<Audit> {
        return remoteDataSource.getAllAuditLogs().map { it.toAuditEntity() }
    }

    override suspend fun addAuditLog(audit: Audit) {
        remoteDataSource.addAuditLog(audit.toAuditDto())
    }

    override suspend fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return remoteDataSource.getAuditLogsByProjectId(projectId).map { it.toAuditEntity() }
    }

    override suspend fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return remoteDataSource.getAuditLogsByTaskId(taskId).map { it.toAuditEntity() }
    }
}
