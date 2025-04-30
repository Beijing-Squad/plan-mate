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

    @Test
    fun `should add audit log for task with state change`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            action = ActionType.UPDATE,
            entityType = EntityType.TASK,
            entityId = "TASK-123",
            oldState = "In Progress",
            newState = "Completed",
            timeStamp = LocalDate(2025, 4, 29)
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        verify { auditRepository.addAuditLog(auditLog) }
    }

    @Test
    fun `should add audit log with DELETE action`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            action = ActionType.DELETE,
            entityType = EntityType.PROJECT,
            entityId = "PROJECT-002",
            timeStamp = LocalDate(2025, 4, 28)
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        verify { auditRepository.addAuditLog(auditLog) }
    }

    @Test
    fun `should add multiple audit logs correctly`() {
        // Given
        val auditLog1 = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            action = ActionType.CREATE,
            entityType = EntityType.TASK,
            entityId = "TASK-456",
            timeStamp = LocalDate(2025, 4, 27)
        )
        val auditLog2 = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            action = ActionType.UPDATE,
            entityType = EntityType.TASK,
            entityId = "TASK-456",
            oldState = "To Do",
            newState = "In Progress",
            timeStamp = LocalDate(2025, 4, 28)
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog1)
        addAuditLogUseCase.addAuditLog(auditLog2)

        // Then
        verify {
            auditRepository.addAuditLog(auditLog1)
            auditRepository.addAuditLog(auditLog2)
        }
    }

    @Test
    fun `should add audit log with minimal fields`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User3",
            action = ActionType.CREATE,
            entityType = EntityType.TASK,
            entityId = "TASK-789",
            oldState = null,
            newState = null,
            timeStamp = LocalDate(2025, 4, 25)
        )

        // When
        addAuditLogUseCase.addAuditLog(auditLog)

        // Then
        verify { auditRepository.addAuditLog(auditLog) }
    }

}