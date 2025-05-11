package ui.screens

import kotlinx.coroutines.runBlocking
import logic.entities.User
import logic.entities.UserRole
import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameException
import logic.exceptions.UnauthorizedUserException
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByIdUseCase
import logic.useCases.user.UpdateUserUseCase
import ui.enums.UserBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

class UserScreen(
    private val getAllUsers: GetAllUsersUseCase,
    private val getUserByUserId: GetUserByIdUseCase,
    private val updateUser: UpdateUserUseCase,
    private val consoleIO: ConsoleIO,
    private val sessionManagerUseCase: SessionManagerUseCase
) : BaseScreen(consoleIO) {
    override val id: String
        get() = "5"
    override val name: String
        get() = "User Screen"

    override fun showOptionService() {
        MenuRenderer.renderMenu(
            """
        ╔════════════════════════════════════════╗
        ║         User Management System         ║
        ╚════════════════════════════════════════╝
        """,
            UserBoardOption.entries,
            consoleIO
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun handleFeatureChoice() {
        while (true){
            when (getInput()) {
                "1" -> {
                    val currentUser = sessionManagerUseCase.getCurrentUser()
                    if (currentUser?.role == UserRole.ADMIN) {
                        onClickGetAllUsers()
                    } else {
                        consoleIO.showWithLine("\u001B[31m❌ You don't have permission\u001B[0m")
                    }
                }

                "2" -> onClickGetUserByID()
                "3" -> onClickUpdateUser()
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")
            }
            showOptionService()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onClickGetAllUsers() {
        consoleIO.showWithLine("\n\u001B[36m📋 All Users:\u001B[0m")

        try {

            showAnimation("get all users...") {
                val users = getAllUsers.getAllUsers()

                if (users.isEmpty()) {
                    consoleIO.showWithLine("\u001B[33m⚠️  No users found.\u001B[0m")
                } else {
                    users.forEach { user ->
                        consoleIO.showWithLine("")
                        consoleIO.showWithLine(
                            """
                        ╭─────────────────────────╮
                        │ ID: ${user.id}
                        │ Username: ${user.userName}
                        │ Role: ${user.role}
                        ╰─────────────────────────╯
                    """.trimIndent()
                        )
                    }
                    consoleIO.showWithLine("\n\u001B[32mTotal users: ${users.size}\u001B[0m")
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ Error: ${e.message ?: "Failed to fetch users"}\u001B[0m")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onClickUpdateUser() {
        consoleIO.showWithLine("\n\u001B[36m✏️ Update User\u001B[0m")
        val userId = sessionManagerUseCase.getCurrentUser()?.id

        runBlocking {
            try {
                val user = getUserByUserId.getUserByUserId(userId.toString())

                showCurrentUserDetails(user)
                updateUserMenu(user)
            } catch (e: Exception) {
                consoleIO.showWithLine("\u001B[31m❌ Error: ${e.message ?: "User not found"}\u001B[0m")
            }
        }

        showOptionService()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun showCurrentUserDetails(user: User) {
        consoleIO.showWithLine(
            """
            ╭─── Current User Details ─────────────╮
            │ ID: ${user.id}
            │ Username: ${user.userName}
            ╰──────────────────────────────────────╯
        """.trimIndent()
        )
    }

    private fun updateUserMenu(user: User) {
        while (true) {
            consoleIO.showWithLine(
                """
                ┌─── Update Options ──────────────────┐
                │                                     │
                │  1. Update Username                 │
                │  2. Update Password                 │
                │  0. Back                            │
                │                                     │
                └─────────────────────────────────────┘
                
                 """
                    .trimIndent()
            )
            consoleIO.show("Choose an option:")

            when (getInput()) {
                "1" -> updateUserName(user)
                "2" -> updatePassword(user)
                "0" -> return
                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid option! Please try again.\u001B[0m")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun updatePassword(user: User) {
        while (true) {
            consoleIO.show("\u001B[32mEnter new password: \u001B[0m")
            val newPassword = getInput()
            consoleIO.show("\u001B[32mConfirm new password: \u001B[0m")
            val confirmPassword = getInput()

            if (newPassword.isNullOrBlank()) {
                consoleIO.showWithLine("\u001B[31m❌ Password cannot be empty!\u001B[0m")
            } else if (newPassword != confirmPassword) {
                consoleIO.showWithLine("\u001B[31m❌ Passwords do not match! Please try again.\u001B[0m")
            } else {
                showAnimation("update password...") {
                    val freshUserData = getUserByUserId.getUserByUserId(user.id.toString())

                    updateUserInSystem(freshUserData.copy(userName = freshUserData.userName, password = newPassword))
                }
                return
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun updateUserName(user: User) {
        consoleIO.show("\u001B[32mEnter new username: \u001B[0m")
        val newUsername = getInput()
        if (!newUsername.isNullOrBlank()) {
            showAnimation("update user name...") {
                val freshUserData = getUserByUserId.getUserByUserId(user.id.toString())

                updateUserInSystem(freshUserData.copy(userName = newUsername, password = freshUserData.password))
            }
            return
        } else {
            consoleIO.showWithLine("\u001B[31m❌ Username cannot be empty!\u001B[0m")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun updateUserInSystem(user: User) {
        try {
            val updatedUser = updateUser.updateUser(user)
            consoleIO.showWithLine("")
            consoleIO.showWithLine(
                """
            ✅ User updated successfully:
            ╭─────────────────────────╮
            │ ID: ${updatedUser.id}
            │ new name: ${updatedUser.userName}
            ╰─────────────────────────╯
        """.trimIndent()
            )
        } catch (invalidUserNameException: InvalidUserNameException) {
            messageFailure(invalidUserNameException)
        } catch (invalidPasswordException: InvalidPasswordException) {
            messageFailure(invalidPasswordException)
        } catch (unauthorizedUserException: UnauthorizedUserException) {
            messageFailure(unauthorizedUserException)
        }
    }

    private fun messageFailure(throwable: Throwable) {
        consoleIO.showWithLine("\u001B[31m❌ Error updating user: ${throwable.message}\u001B[0m")
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun onClickGetUserByID() {
        consoleIO.showWithLine("\n\u001B[36m🔍 Find User by ID\u001B[0m")
        consoleIO.show("\u001B[32mEnter user ID: \u001B[0m")
        val userId = getInput()

        showAnimation("get user by id...") {
            try {
                val user = userId?.let { getUserByUserId.getUserByUserId(it) }
                consoleIO.showWithLine("")
                consoleIO.showWithLine(
                    """
            ╭─────────────────────────╮
            │ User Found:
            │ ID: ${user?.id}
            │ Username: ${user?.userName}
            │ role: ${user?.role}
            ╰─────────────────────────╯
        """.trimIndent()
                )
            } catch (e: Exception) {
                consoleIO.showWithLine("\u001B[31m❌ Error: ${e.message ?: "User not found"}\u001B[0m")
            }
        }
    }
}