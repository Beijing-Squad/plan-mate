package ui.screens

import logic.entities.User
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import ui.main.BaseScreen
import ui.main.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

class UserScreen(
    private val getAllUsers: GetAllUsersUseCase,
    private val getUserByUserId: GetUserByUserIdUseCase,
    private val updateUser: UpdateUserUseCase,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {
    override val id: String
        get() = "1"
    override val name: String
        get() = "User Screen"


    override fun showOptionService() {
        consoleIO.show("\u001B[32mEnter option: \u001B[0m")
        consoleIO.showWithLine(
            """
            \u001B[36m╔════════════════════════════════╗
            ║      User Management     ║
            ╚════════════════════════════════╝\u001B[0m
            
            \u001B[33m1\u001B[0m. 📋 List All Users
            \u001B[33m2\u001B[0m. 🔍 Find User by ID
            \u001B[33m3\u001B[0m. ✏️ Update User
            \u001B[33m0\u001B[0m. 🔙 Exit to Main Menu
            
            \u001B[32mPlease select an option:\u001B[0m """.trimIndent()
        )
    }

    override fun handleFeatureChoice() {
        when (getInput()) {
            "1" -> getAllUsers()
            "2" -> getUserByID()
            "3" -> updateUser()
            "0" -> return
            else -> consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun getAllUsers() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Users:\u001B[0m")
        val users = getAllUsers.getAllUsers()

        if (users.isEmpty()) {
            consoleIO.showWithLine("\u001B[33m⚠️  No users found.\u001B[0m")
        } else {
            users.forEach { user ->
                consoleIO.showWithLine(
                    """
                \u001B[32m╭─────────────────────────╮
                │ ID: ${user.id}
                │ Username: ${user.userName}
                │ Role: ${user.role}
                ╰─────────────────────────╯\u001B[0m
            """.trimIndent()
                )
            }
            consoleIO.showWithLine("\n\u001B[32mTotal users: ${users.size}\u001B[0m")
        }
    }

    private fun updateUser() {
        updateUser
            .updateUser(getUserByUserId.getUserByUserId("117ae359-8dac-443f-a132-35b015b4a811"))
            .fold(
                onSuccess = ::onUpdateUserSuccess,
                onFailure = ::onUpdateUserFailer
            )
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onUpdateUserSuccess(user: User) {
        consoleIO.showWithLine(
            """
            \u001B[32m✅ User updated successfully:
            ╭─────────────────────────╮
            │ ID: ${user.id}
            │ Username: ${user.userName}
            │ Role: ${user.role}
            ╰─────────────────────────╯\u001B[0m
        """.trimIndent()
        )
    }


    private fun onUpdateUserFailer(throwable: Throwable) {
        consoleIO.showWithLine("\u001B[31m❌ Error updating user: ${throwable.message}\u001B[0m")
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun getUserByID() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find User by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter user ID: \u001B[0m")
        val userId = getInput()

        try {
            val user = userId?.let { getUserByUserId.getUserByUserId(it) }
            consoleIO.showWithLine("""
            \u001B[32m╭─────────────────────────╮
            │ User Found:
            │ ID: ${user?.id}
            │ Username: ${user?.userName}
            ╰─────────────────────────╯\u001B[0m
        """.trimIndent())
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Error: ${e.message ?: "User not found"}\u001B[0m")
        }
    }


}