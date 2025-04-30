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


}