package logic.useCases.audit

import io.mockk.mockk
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk()
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }


}