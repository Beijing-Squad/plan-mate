package ui.screens

import logic.entities.User
import logic.entities.UserRole
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManager
import org.koin.mp.KoinPlatform.getKoin
import ui.main.PlanMateConsoleUi

class AuthenticationScreen(
    private val registerUseCase: RegisterUserAuthenticationUseCase,
    private val loginUseCase: LoginUserAuthenticationUseCase,
    private val sessionManager: SessionManager
) {

    fun start(): AuthResult {
        while (true) {
            println("\n=== Welcome to PlanMate CLI ===")
            println("1. Login")
            println("2. Register")
            println("0. Exit")
            print("Choose an option: ")

            when (readln().trim()) {
                "1" -> {
                    val user = login()
                    if (user != null) {
                        return AuthResult.Success(user)
                    }
                }

                "2" -> register()
                "0" -> return AuthResult.Exit
                else -> println("❌ Invalid option. Please try again.")
            }
        }
    }

    private fun login(): User? {
        println("\n--- Login ---")
        print("Username: ")
        val username = readln().trim()
        print("Password: ")
        val password = readln().trim()

        return try {
            val user = loginUseCase.execute(username, password)
            println("✅ Login successful. Welcome, ${sessionManager.getCurrentUser()?.userName}!")
            val planMateConsoleUi: PlanMateConsoleUi = getKoin().get()
            planMateConsoleUi.start()
            user
        } catch (e: Exception) {
            println("❌ ${e.message}")
            null
        }
    }

    private fun register() {
        println("\n--- Register ---")
        print("Username: ")
        val username = readln().trim()
        print("Password: ")
        val password = readln().trim()
        val role = selectRole()

        try {
            registerUseCase.execute(username, password, role)
            println("✅ Registration successful. You can now login.")
        } catch (e: Exception) {
            println("❌ Registration failed: ${e.message}")
        }
    }

    private fun selectRole(): UserRole {
        while (true) {
            println("Select Role:")
            println("1. Admin")
            println("2. Mate")
            print("Enter choice: ")

            return when (readln().trim()) {
                "1" -> UserRole.ADMIN
                "2" -> UserRole.MATE
                else -> {
                    println("❌ Invalid role. Please choose again.")
                    continue
                }
            }
        }
    }

    sealed class AuthResult {
        data class Success(val user: User) : AuthResult()
        data object Exit : AuthResult()
    }
}