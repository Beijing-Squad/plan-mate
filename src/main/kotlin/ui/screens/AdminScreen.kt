package ui.screens

import logic.useCases.authentication.SessionManagerUseCase
import ui.main.consoleIO.ConsoleIO

class AdminScreen(
    private val projectManagementScreen: ProjectManagementScreen,
    private val stateScreen: StateScreen,
    private val taskManagementScreen: TaskManagementScreen,
    private val auditScreen: AuditScreen,
    private val userScreen: UserScreen,
    authenticationScreen: AuthenticationScreen,
    sessionManagerUseCase: SessionManagerUseCase,
    consoleIO: ConsoleIO
) : DashboardScreen(sessionManagerUseCase, consoleIO, authenticationScreen) {

    override val role = "ADMIN"
    override val header = "PlanMate Admin Dashboard 🚀"

    override fun presentFeatures() {
        consoleIO.showWithLine("\n╔═══════════════════════════════════════════════╗")
        consoleIO.showWithLine("║          ADMIN CONTROL PANEL                  ║")
        consoleIO.showWithLine("╚═══════════════════════════════════════════════╝")
        consoleIO.showWithLine("${projectManagementScreen.id}. ${projectManagementScreen.name}")
        consoleIO.showWithLine("${stateScreen.id}. ${stateScreen.name}")
        consoleIO.showWithLine("${taskManagementScreen.id}. ${taskManagementScreen.name}")
        consoleIO.showWithLine("${auditScreen.id}. ${auditScreen.name}")
        consoleIO.showWithLine("${userScreen.id}. ${userScreen.name}")
        consoleIO.showWithLine("0. Logout")
        consoleIO.show("\uD83D\uDCA1 Enter choice: ")
    }

    override fun processInput(input: String): Boolean {
        return when (input) {
            projectManagementScreen.id -> {
                projectManagementScreen.execute(); true
            }

            stateScreen.id -> {
                stateScreen.execute(); true
            }

            taskManagementScreen.id -> {
                taskManagementScreen.execute(); true
            }

            auditScreen.id -> {
                auditScreen.execute(); true
            }

            userScreen.id -> {
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