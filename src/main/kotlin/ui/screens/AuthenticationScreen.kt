package ui.screens

import logic.entities.UserRole
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import java.util.*

class AuthenticationScreen(
    private val registerUseCase: RegisterUserAuthenticationUseCase,
    private val loginUseCase: LoginUserAuthenticationUseCase
) {
    private val scanner = Scanner(System.`in`)

    fun start(): AuthResult {
        while (true) {
            println("\n=== Welcome to PlanMate CLI ===")
            println("1. Login")
            println("2. Register")
            println("0. Exit")
            print("Choose an option: ")

            when (scanner.nextLine()) {
                "1" -> {
                    val username = login() ?: continue
                    return AuthResult.Success(username)
                }

                "2" -> register()
                "0" -> return AuthResult.Exit
                else -> println("❌ Invalid option. Please try again.")
            }
        }
    }

    private fun login(): String? {
        println("\n--- Login ---")
        print("Username: ")
        val username = scanner.nextLine().trim()
        print("Password: ")
        val password = scanner.nextLine().trim()

        return try {
            val success = loginUseCase.execute(username, password)
            if (success) {
                println("✅ Login successful. Welcome, $username!")
                username
            } else {
                println("❌ Login failed.")
                null
            }
        } catch (e: Exception) {
            println("❌ ${e.message}")
            null
        }
    }

    private fun register() {
        println("\n--- Register ---")
        print("Username: ")
        val username = scanner.nextLine().trim()
        print("Password: ")
        val password = scanner.nextLine().trim()
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

            return when (scanner.nextLine()) {
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
        data class Success(val username: String) : AuthResult()
        data object Exit : AuthResult()
    }
}