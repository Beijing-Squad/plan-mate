package logic.useCases.audit

import fake.createAudit
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.UserRole
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddAuditLogUseCaseTest {
    private lateinit var auditRepository: AuditRepository
    private lateinit var addAuditLogUseCase: AddAuditLogUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        addAuditLogUseCase = AddAuditLogUseCase(auditRepository)
    }

    @Test
    fun `should add audit log when called`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.CREATE,
            entityType = EntityType.PROJECT,
            entityId = "PROJECT-001",
            timeStamp = LocalDate(2025, 4, 30)
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        verify { auditRepository.addAuditLog(auditLog) }
    }

}