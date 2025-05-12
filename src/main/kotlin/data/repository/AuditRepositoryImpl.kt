package data.repository

import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.repository.mapper.toAuditDTO
import data.repository.mapper.toAuditEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : AuditRepository {

    override suspend fun getAllAuditLogs(): List<Audit> {
        return remoteDataSource.getAllAuditLogs().map { toAuditEntity(it) }
    }

    override suspend fun addAuditLog(audit: Audit) {
        remoteDataSource.addAuditLog(toAuditDTO(audit))
    }

    override suspend fun getAuditLogsByProjectId(projectId: String): List<Audit> {
        return remoteDataSource.getAuditLogsByProjectId(projectId).map { toAuditEntity(it) }
    }

    override suspend fun getAuditLogsByTaskId(taskId: String): List<Audit> {
        return remoteDataSource.getAuditLogsByTaskId(taskId).map { toAuditEntity(it) }
    }
}
