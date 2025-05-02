package ui

import io.mockk.*
import org.junit.Before
import org.junit.Test
import ui.service.ConsoleIOService
import ui.serviceImpl.*

class MainMenuControllerTest {

    private lateinit var controller: MainMenuController

    private val authUIService = mockk<AuthUIServiceImpl>(relaxed = true)
    private val projectUIService = mockk<ProjectUIServiceImpl>(relaxed = true)
    private val taskUIService = mockk<TaskUIServiceImpl>(relaxed = true)
    private val stateUIService = mockk<StateUIServiceImpl>(relaxed = true)
    private val auditUIService = mockk<AuditUIServiceImpl>(relaxed = true)
    private val swimlaneUIService = mockk<SwimlaneUIServiceImpl>(relaxed = true)
    private val console = mockk<ConsoleIOService>(relaxed = true)

    @Before
    fun setup() {
        controller = MainMenuController(
            authUIService,
            projectUIService,
            taskUIService,
            stateUIService,
            auditUIService,
            swimlaneUIService,
            console
        )
    }

    @Test
    fun `showMainMenu should show error and exit if login fails`() {
        every { authUIService.login() } returns false

        controller.showMainMenu()

        verify { console.printError("Login failed. Exiting...") }
    }

    @Test
    fun `showMainMenu should call adminMenuLoop for Admin role`() {
        every { authUIService.login() } returns true
        every { authUIService.getUserRole() } returns "Admin"
        every { console.readNonEmptyLine(any()) } returns "7"

        controller.showMainMenu()

        verify { console.printMessage("Logging out...") }
    }

    @Test
    fun `showMainMenu should call mateMenuLoop for Mate role`() {
        every { authUIService.login() } returns true
        every { authUIService.getUserRole() } returns "Mate"
        every { console.readNonEmptyLine(any()) } returns "4" 

        controller.showMainMenu()

        verify { console.printMessage("Logging out...") }
    }

    @Test
    fun `showMainMenu should show error for unknown role`() {
        every { authUIService.login() } returns true
        every { authUIService.getUserRole() } returns "Unknown"

        controller.showMainMenu()

        verify { console.printError("Unknown role. Exiting...") }
    }
}
