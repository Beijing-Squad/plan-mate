package logic.useCases.audit

import data.repository.AuditRepositoryImpl
import data.repository.dataSource.AuditDataSource
import io.mockk.mockk
import logic.entities.Audit
import logic.repository.AuditRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class GetAllAuditLogsUseCaseTest {

    lateinit var getAllAuditLogs: GetAllAuditLogsUseCase
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockk(relaxed = true))
        getAllAuditLogs = GetAllAuditLogsUseCase(auditRepository)
    }

}