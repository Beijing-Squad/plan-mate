package ui.screens

import logic.useCases.authentication.SessionManagerUseCase
import ui.main.consoleIO.ConsoleIO

class MateScreen(
    private val taskManagementScreen: TaskManagementScreen,
    private val auditScreen: AuditScreen,
    private val userScreen: UserScreen,
    authenticationScreen: AuthenticationScreen,
    sessionManagerUseCase: SessionManagerUseCase,
    consoleIO: ConsoleIO
) : DashboardScreen(sessionManagerUseCase, consoleIO ,authenticationScreen) {

    override val role = "MATE"
    override val header = "PlanMate Dashboard 📋"

    override fun presentFeatures() {
        consoleIO.showWithLine("\n╔═══════════════════════════════════════════════╗")
        consoleIO.showWithLine("║          MATE DASHBOARD                       ║")
        consoleIO.showWithLine("╚═══════════════════════════════════════════════╝")
        consoleIO.showWithLine("1. ${taskManagementScreen.name}")
        consoleIO.showWithLine("2. ${auditScreen.name}")
        consoleIO.showWithLine("3. ${userScreen.name}")
        consoleIO.showWithLine("0. Logout")
        consoleIO.show("\uD83D\uDCA1 Enter choice: ")
    }

    override fun processInput(input: String): Boolean {
        return when (input) {
            "1" -> {
                taskManagementScreen.execute(); true
            }
            "2" -> {
                auditScreen.execute(); true
            }
            "3" -> {
                userScreen.execute(); true
            }
            "0" -> {
                logoutUser(); false
            }
            else -> {
                consoleIO.showWithLine("❌ Invalid input! Please choose one of the available options.")
                true
            }
        }
    }
}