package logic.useCases.audit

import fake.createAudit
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Audit
import logic.entities.type.UserRole
import logic.repository.AuditRepository
import logic.useCases.authentication.SessionManagerUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddAuditLogUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var addAuditLogUseCase: AddAuditLogUseCase
    private lateinit var sessionManagerUseCase: SessionManagerUseCase

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        addAuditLogUseCase = AddAuditLogUseCase(auditRepository,sessionManagerUseCase)
    }

    @Test
    fun `should add audit log when called`() = runTest {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-001"
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        coVerify { auditRepository.addAuditLog(auditLog) }
    }

    @Test
    fun `should add audit log for task with state change`() = runTest {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-123"
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        coVerify { auditRepository.addAuditLog(auditLog) }
    }

    @Test
    fun `should add audit log with DELETE action`() = runTest {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            action = Audit.ActionType.DELETE,
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-002"
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        coVerify { auditRepository.addAuditLog(auditLog) }
    }

    @Test
    fun `should add multiple audit logs correctly`() = runTest {
        // Given
        val auditLog1 = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-456"
        )
        val auditLog2 = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-456"
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog1)
        addAuditLogUseCase.addAuditLog(auditLog2)

        // Then
        coVerify {
            auditRepository.addAuditLog(auditLog1)
            auditRepository.addAuditLog(auditLog2)
        }
    }

    @Test
    fun `should add audit log with minimal fields`() = runTest {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User3",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-789"
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        coVerify { auditRepository.addAuditLog(auditLog) }
    }
}
