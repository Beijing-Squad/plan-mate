package ui.screens

import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import fake.createAudit
import io.mockk.*
import kotlinx.coroutines.runBlocking
import logic.entity.Audit
import logic.entity.type.UserRole
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import ui.main.consoleIO.ConsoleIO
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuditScreenTest {

    private lateinit var getAllAuditLogsUseCase: GetAllAuditLogsUseCase
    private lateinit var getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase
    private lateinit var getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var auditScreen: AuditScreen

    private val allAudit = listOf(
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.CREATE,
            entityType = Audit.EntityType.PROJECT,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.TASK,
        ),
        createAudit(
            userRole = UserRole.ADMIN,
            userName = "Adel",
            action = Audit.ActionType.UPDATE,
            entityType = Audit.EntityType.PROJECT,
        )
    )

    @BeforeTest
    fun setUp() {
        getAllAuditLogsUseCase = mockk(relaxed = true)
        getAuditLogsByProjectIdUseCase = mockk(relaxed = true)
        getAuditLogsByTaskIdUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        auditScreen = AuditScreen(
            getAllAuditLogsUseCase,
            getAuditLogsByProjectIdUseCase,
            getAuditLogsByTaskIdUseCase,
            consoleIO
        )
    }

    @Test
    fun `handleFeatureChoice shows all audit logs if not empty`() = runBlocking {
        every { consoleIO.read() } returns "1"
        coEvery { getAllAuditLogsUseCase.getAllAuditLogs() } returns allAudit

        auditScreen.handleFeatureChoice()

        verify {
            consoleIO.showWithLine("\n📋 All Audit Logs:\n")
            consoleIO.showWithLine(match { it.contains("Adel") })
        }
    }

    @Test
    fun `handleFeatureChoice shows no audit logs if list is empty`() = runBlocking {
        every { consoleIO.read() } returns "1"
        coEvery { getAllAuditLogsUseCase.getAllAuditLogs() } returns emptyList()

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ No Audit Logs Found") }
    }

    @Test
    fun `handleFeatureChoice shows project logs if found`() = runBlocking {
        val projectId = "123"
        every { consoleIO.read() } returns "2" andThen projectId
        coEvery { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId) } returns allAudit

        auditScreen.handleFeatureChoice()

        verify {
            consoleIO.showWithLine("\n🔍 Audit Logs For Project ID: $projectId\n")
            consoleIO.showWithLine(match { it.contains("Adel") })
        }
    }

    @Test
    fun `handleFeatureChoice shows not found for empty project logs`() = runBlocking {
        val projectId = "123"
        every { consoleIO.read() } returns "2" andThen projectId
        coEvery { getAuditLogsByProjectIdUseCase.getAuditLogsByProjectId(projectId) } returns emptyList()

        auditScreen.handleFeatureChoice()

        verify(atLeast = 1) {
            consoleIO.show("Enter ID: ")
            consoleIO.showWithLine("❌ No Audit Logs Found")
            consoleIO.show(match { it.contains("Fetching project audit logs...") })
        }

    }

    @Test
    fun `handleFeatureChoice shows invalid project id if blank`() = runBlocking {
        every { consoleIO.read() } returns "2" andThen ""
        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ Error: ID shouldn't be blank") }
    }

    @Test
    fun `handleFeatureChoice shows task logs if found`() = runBlocking {
        val taskId = "456"
        every { consoleIO.read() } returns "3" andThen taskId
        coEvery { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId) } returns allAudit

        auditScreen.handleFeatureChoice()

        verify {
            consoleIO.showWithLine("\n🔍 Audit Logs For Task ID: $taskId\n")
            consoleIO.showWithLine(match { it.contains("Adel") })
        }
    }

    @Test
    fun `handleFeatureChoice shows not found for empty task logs`() = runBlocking {
        val taskId = "456"
        every { consoleIO.read() } returns "3" andThen taskId
        coEvery { getAuditLogsByTaskIdUseCase.getAuditLogsByTaskId(taskId) } returns emptyList()

        auditScreen.handleFeatureChoice()

        verify { consoleIO.showWithLine("❌ No Audit Logs Found") }
    }

    @Test
    fun `handleFeatureChoice shows invalid task id if blank`() = runBlocking {
        every { consoleIO.read() } returns "3" andThen ""

        auditScreen.handleFeatureChoice()

        verify {
            consoleIO.showWithLine("❌ Error: ID shouldn't be blank")
        }
    }


    @Test
    fun `handleFeatureChoice exits when 0 selected`() = runBlocking {
        every { consoleIO.read() } returns "0"
        auditScreen.handleFeatureChoice()
        confirmVerified(getAllAuditLogsUseCase, getAuditLogsByProjectIdUseCase, getAuditLogsByTaskIdUseCase)
    }

    @Test
    fun `handleFeatureChoice shows invalid option message`() = runBlocking {
        every { consoleIO.read() } returns "9"
        auditScreen.handleFeatureChoice()
        verify { consoleIO.showWithLine("❌ Invalid Option") }
    }
}
