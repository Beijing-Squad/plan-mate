package ui.screens

import kotlinx.coroutines.runBlocking
import logic.entities.User
import logic.entities.type.UserRole
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManagerUseCase
import org.koin.mp.KoinPlatform.getKoin
import ui.main.consoleIO.ConsoleIO
import kotlin.system.exitProcess

class AuthenticationScreen(
    private val registerUseCase: RegisterUserAuthenticationUseCase,
    private val loginUseCase: LoginUserAuthenticationUseCase,
    private val sessionManagerUseCase: SessionManagerUseCase,
    private val consoleIO: ConsoleIO
) {

    fun start(): User {
        while (true) {
            consoleIO.showWithLine("\n=== Welcome to PlanMate CLI ===")
            consoleIO.showWithLine("1. Login")
            consoleIO.showWithLine("2. Register")
            consoleIO.showWithLine("0. Exit")
            consoleIO.show("Choose an option: ")

            when (consoleIO.read()!!.trim()) {
                "1" -> {
                    val user = runBlocking { login() }
                    if (user != null) return user
                }

                "2" -> runBlocking { register() }
                "0" -> exitProcess(0)
                else -> consoleIO.showWithLine("❌ Invalid option. Please try again.")
            }
        }
    }

    private suspend fun login(): User? {
        consoleIO.showWithLine("\n--- Login ---")
        consoleIO.show("Username: ")
        val username = consoleIO.read()?.trim()

        if (username.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Username cannot be empty.")
            return null
        }

        consoleIO.show("Password: ")
        val password = consoleIO.read()?.trim()

        if (password.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Password cannot be empty.")
            return null
        }

        return try {
            val user = loginUseCase.execute(username, password)
            consoleIO.showWithLine("✅ Login successful. Welcome, ${sessionManagerUseCase.getCurrentUser()?.userName}!")

            when (user.role) {
                UserRole.ADMIN -> {
                    val adminScreen: AdminScreen = getKoin().get()
                    adminScreen.start()
                }

                UserRole.MATE -> {
                    val mateScreen: MateScreen = getKoin().get()
                    mateScreen.start()
                }
            }

            user
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ ${e.message}")
            null
        }
    }

    private suspend fun register() {
        consoleIO.showWithLine("\n--- Register ---")
        consoleIO.show("Username: ")
        val username = consoleIO.read()?.trim()

        if (username.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Username cannot be empty.")
            return
        }

        consoleIO.show("Password: ")
        val password = consoleIO.read()?.trim()

        if (password.isNullOrBlank()) {
            consoleIO.showWithLine("❌ Password cannot be empty.")
            return
        }

        val role = selectRole()

        try {
            registerUseCase.execute(username, password, role)
            consoleIO.showWithLine("✅ Registration successful. You can now login.")
        } catch (e: Exception) {
            consoleIO.showWithLine("❌ Registration failed: ${e.message}")
        }
    }

    private fun selectRole(): UserRole {
        while (true) {
            consoleIO.showWithLine("Select Role:")
            consoleIO.showWithLine("1. Admin")
            consoleIO.showWithLine("2. Mate")
            consoleIO.show("Enter choice: ")

            return when (consoleIO.read()!!.trim()) {
                "1" -> UserRole.ADMIN
                "2" -> UserRole.MATE
                else -> {
                    consoleIO.showWithLine("❌ Invalid role. Please choose again.")
                    continue
                }
            }
        }
    }
}