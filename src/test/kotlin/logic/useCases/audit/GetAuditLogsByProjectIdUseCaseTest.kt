package logic.useCases.audit

import com.google.common.truth.Truth.assertThat
import fake.createAudit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.ActionType
import logic.entities.EntityType
import logic.entities.type.UserRole
import logic.exceptions.InvalidInputException
import logic.exceptions.ProjectNotFoundException
import logic.repository.AuditRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAuditLogsByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase

    private val allAudit = listOf(
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.CREATE,
            entityType = EntityType.PROJECT,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.PROJECT,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = ActionType.UPDATE,
            entityType = EntityType.PROJECT,
        )
    )

    @BeforeEach
    fun setUp() {
        auditRepository = mockk(relaxed = true)
        getAuditLogsByProjectIdUseCase = GetAuditLogsByProjectIdUseCase(auditRepository)
    }

    @Test
    fun `should return list of audit logs when project id is found`() = runTest {
        // Given
        val givenId = "PROJECT-001"
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit
        coEvery { auditRepository.getAuditLogsByProjectId(givenId) } returns listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.CREATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Adel",
                action = ActionType.UPDATE,
                entityType = EntityType.PROJECT,
                entityId = givenId,
            )
        )

        // When
        val result = getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should throw ProjectNotFoundException when project id not found`() = runTest {
        // Given
        val givenId = "PROJECT-001"
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit
        coEvery { auditRepository.getAuditLogsByProjectId(givenId) } returns emptyList()

        // When && Then
        assertThrows<ProjectNotFoundException> {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)
        }
    }

    @Test
    fun `should throw InvalidInputException when project id is blank`() = runTest {
        // Given
        val givenId = " "
        coEvery { auditRepository.getAllAuditLogs() } returns allAudit

        // When && Then
        assertThrows<InvalidInputException> {
            getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(givenId)
        }
    }
}
