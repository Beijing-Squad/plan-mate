package logic.useCases.audit

import fake.createAudit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entity.Audit
import logic.entity.type.UserRole
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
        sessionManagerUseCase = mockk()
        addAuditLogUseCase = AddAuditLogUseCase(auditRepository, sessionManagerUseCase)
    }

    @Test
    fun `should add audit log when called`() = runTest {
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-001"
        )

        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { role } returns auditLog.userRole
            every { userName } returns auditLog.userName
        }

        addAuditLogUseCase.addAuditLog(auditLog)

        coVerify {
            auditRepository.addAuditLog(match {
                it.userName == "Adel" &&
                        it.action == Audit.ActionType.CREATE &&
                        it.entityType == Audit.EntityType.PROJECT &&
                        it.userRole == UserRole.ADMIN
            })
        }
    }

    @Test
    fun `should add audit log for task with state change`() = runTest {
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-123"
        )

        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { role } returns auditLog.userRole
            every { userName } returns auditLog.userName
        }

        addAuditLogUseCase.addAuditLog(auditLog)

        coVerify {
            auditRepository.addAuditLog(match {
                it.userName == "User1" &&
                        it.action == Audit.ActionType.UPDATE &&
                        it.entityType == Audit.EntityType.TASK &&
                        it.userRole == UserRole.MATE
            })
        }
    }

    @Test
    fun `should add audit log with DELETE action`() = runTest {
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            action = Audit.ActionType.DELETE,
            entityType = Audit.EntityType.PROJECT,
            entityId = "PROJECT-002"
        )

        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { role } returns auditLog.userRole
            every { userName } returns auditLog.userName
        }

        addAuditLogUseCase.addAuditLog(auditLog)

        coVerify {
            auditRepository.addAuditLog(match {
                it.userName == "Admin" &&
                        it.action == Audit.ActionType.DELETE &&
                        it.entityType == Audit.EntityType.PROJECT &&
                        it.userRole == UserRole.ADMIN
            })
        }
    }

    @Test
    fun `should add multiple audit logs correctly`() = runTest {
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

        coEvery { sessionManagerUseCase.getCurrentUser() } returnsMany listOf(
            mockk {
                every { role } returns auditLog1.userRole
                every { userName } returns auditLog1.userName
            },
            mockk {
                every { role } returns auditLog2.userRole
                every { userName } returns auditLog2.userName
            }
        )

        addAuditLogUseCase.addAuditLog(auditLog1)
        addAuditLogUseCase.addAuditLog(auditLog2)

        coVerify {
            auditRepository.addAuditLog(match {
                it.userName == "User1" &&
                        it.userRole == UserRole.MATE
            })
            auditRepository.addAuditLog(match {
                it.userName == "Admin" &&
                        it.userRole == UserRole.ADMIN
            })
        }
    }

    @Test
    fun `should add audit log with minimal fields`() = runTest {
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User3",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.TASK,
            entityId = "TASK-789"
        )

        coEvery { sessionManagerUseCase.getCurrentUser() } returns mockk {
            every { role } returns auditLog.userRole
            every { userName } returns auditLog.userName
        }

        addAuditLogUseCase.addAuditLog(auditLog)

        coVerify {
            auditRepository.addAuditLog(match {
                it.userName == "User3" &&
                        it.action == Audit.ActionType.CREATE &&
                        it.entityType == Audit.EntityType.TASK &&
                        it.userRole == UserRole.MATE
            })
        }
    }
}
