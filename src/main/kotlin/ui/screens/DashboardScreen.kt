package ui.screens

import logic.useCases.authentication.SessionManagerUseCase
import ui.main.PlanMateUi
import ui.main.consoleIO.ConsoleIO

abstract class DashboardScreen(
    private val sessionManagerUseCase: SessionManagerUseCase,
    protected val consoleIO: ConsoleIO,
    private val authenticationScreen: AuthenticationScreen
) : PlanMateUi {

    abstract val role: String
    abstract val header: String

    override fun start() {
        try {
            showWelcome()
            runMenuLoop()
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unknown error occurred"
            consoleIO.showWithLine("❌ $errorMessage")
        }
    }

    private fun showWelcome() {
        val currentUser = sessionManagerUseCase.getCurrentUser()
        consoleIO.showWithLine("\n╔═══════════════════════════════════════════════╗")
        consoleIO.showWithLine("║   Welcome to $header                          ║")
        consoleIO.showWithLine("╚═══════════════════════════════════════════════╝")
        consoleIO.showWithLine("👤 Logged in as: ${currentUser?.userName} ($role)")
    }

    private fun runMenuLoop() {
        var running = true
        while (running) {
            presentFeatures()
            val input = consoleIO.read()
            input?.let { running = processInput(it) }
        }
    }

    abstract fun presentFeatures()
    abstract fun processInput(input: String): Boolean

    protected fun logoutUser() {
        sessionManagerUseCase.clearCurrentUser()
        consoleIO.showWithLine("✅ Successfully logged out.")
        authenticationScreen.start() // Redirect to authentication screen
    }
}