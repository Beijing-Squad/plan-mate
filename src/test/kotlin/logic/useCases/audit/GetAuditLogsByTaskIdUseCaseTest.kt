package logic.useCases.audit

import data.repository.AuditRepositoryImpl
import io.mockk.mockk
import logic.repository.AuditRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class GetAuditLogsByTaskIdUseCaseTest {

    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepositoryImpl(mockk(relaxed = true))
        getAuditLogsByTaskIdUseCase = GetAuditLogsByTaskIdUseCase(auditRepository)
    }

}