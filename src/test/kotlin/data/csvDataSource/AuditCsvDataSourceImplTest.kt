package data.csvDataSource

import com.google.common.truth.Truth.assertThat
import data.csvDataSource.csv.CsvDataSourceImpl
import fake.createAudit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.UserRole
import logic.entities.exceptions.DataAccessException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuditCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<Audit>
    private lateinit var auditDataSource: AuditCsvDataSourceImpl

    @BeforeEach
    fun setUp() {
        csvDataSource = mockk(relaxed = true)
        auditDataSource = AuditCsvDataSourceImpl(csvDataSource)
    }

    @Test
    fun `should return all audit logs from CSV`() {
        // Given
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = ActionType.CREATE,
                timeStamp = LocalDate(2025, 4, 30)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = "TASK-123",
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2025, 4, 29)
            )
        )
        every { csvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = auditDataSource.getAllAuditLogs()

        // Then
        assertThat(result).isEqualTo(auditLogs)
        verify { csvDataSource.loadAllDataFromFile() }
    }

    // Test Case 2: getAllAuditLogs with empty CSV
    @Test
    fun `should return empty list when CSV is empty`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } returns emptyList()

        // When
        val result = auditDataSource.getAllAuditLogs()

        // Then
        assertThat(result).isEmpty()
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should throw DataAccessException when CSV read fails`() {
        // Given
        every { csvDataSource.loadAllDataFromFile() } throws DataAccessException("Failed to read audit.csv")

        // When&&Then
        val exception = assertThrows<DataAccessException> {
            auditDataSource.getAllAuditLogs()
        }
        assertThat(exception.message).isEqualTo("Failed to read audit.csv")
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should append audit log to CSV`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            entityType = EntityType.PROJECT,
            entityId = "PROJECT-001",
            action = ActionType.CREATE,
            timeStamp = LocalDate(2025, 4, 30)
        )

        // When
        auditDataSource.addAuditLog(auditLog)

        // Then
        verify { csvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should append audit log with state change to CSV`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.MATE,
            userName = "User1",
            entityType = EntityType.TASK,
            entityId = "TASK-123",
            action = ActionType.UPDATE,
            oldState = "In Progress",
            newState = "Completed",
            timeStamp = LocalDate(2025, 4, 29)
        )

        // When
        auditDataSource.addAuditLog(auditLog)

        // Then
        verify { csvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should throw DataAccessException when CSV write fails`() {
        // Given
        val auditLog = createAudit(
            userRole = UserRole.ADMIN,
            userName = "Admin",
            entityType = EntityType.PROJECT,
            entityId = "PROJECT-002",
            action = ActionType.DELETE,
            timeStamp = LocalDate(2025, 4, 28)
        )
        every { csvDataSource.appendToFile(auditLog) } throws DataAccessException("Failed to write to audit.csv")

        // When&&Then
        val exception = assertThrows<DataAccessException> {
            auditDataSource.addAuditLog(auditLog)
        }
        assertThat(exception.message).isEqualTo("Failed to write to audit.csv")
        verify { csvDataSource.appendToFile(auditLog) }
    }

    @Test
    fun `should return audit logs for a specific project ID`() {
        // Given
        val projectId = "PROJECT-001"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = projectId,
                action = ActionType.CREATE,
                timeStamp = LocalDate(2025, 4, 30)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = "TASK-123",
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = projectId,
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2025, 4, 28)
            )
        )
        every { csvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = auditDataSource.getAuditLogsByProjectId(projectId)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == projectId && it.entityType == EntityType.PROJECT }).isTrue()
        assertThat(result.map { it.action }).containsExactly(ActionType.CREATE, ActionType.UPDATE)
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no audit logs match project ID`() {
        // Given
        val projectId = "PROJECT-999"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = "TASK-123",
                action = ActionType.UPDATE,
                timeStamp = LocalDate(2025, 4, 29)
            )
        )
        every { csvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = auditDataSource.getAuditLogsByProjectId(projectId)

        // Then
        assertThat(result).isEmpty()
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return audit logs for a specific task ID`() {
        // Given
        val taskId = "TASK-123"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.MATE,
                userName = "User1",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.UPDATE,
                oldState = "In Progress",
                newState = "Completed",
                timeStamp = LocalDate(2025, 4, 29)
            ),
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = ActionType.CREATE,
                timeStamp = LocalDate(2025, 4, 30)
            ),
            createAudit(
                userRole = UserRole.MATE,
                userName = "User2",
                entityType = EntityType.TASK,
                entityId = taskId,
                action = ActionType.DELETE,
                timeStamp = LocalDate(2025, 4, 28)
            )
        )
        every { csvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = auditDataSource.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all { it.entityId == taskId && it.entityType == EntityType.TASK }).isTrue()
        assertThat(result.map { it.action }).containsExactly(ActionType.UPDATE, ActionType.DELETE)
        verify { csvDataSource.loadAllDataFromFile() }
    }

    @Test
    fun `should return empty list when no audit logs match task ID`() {
        // Given
        val taskId = "TASK-999"
        val auditLogs = listOf(
            createAudit(
                userRole = UserRole.ADMIN,
                userName = "Admin",
                entityType = EntityType.PROJECT,
                entityId = "PROJECT-001",
                action = ActionType.CREATE,
                timeStamp = LocalDate(2025, 4, 30)
            )
        )
        every { csvDataSource.loadAllDataFromFile() } returns auditLogs

        // When
        val result = auditDataSource.getAuditLogsByTaskId(taskId)

        // Then
        assertThat(result).isEmpty()
        verify { csvDataSource.loadAllDataFromFile() }
    }

}