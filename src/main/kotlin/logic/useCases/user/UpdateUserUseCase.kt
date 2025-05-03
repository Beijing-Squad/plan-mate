package logic.useCases.user

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UnauthorizedUserException
import logic.repository.UserRepository
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.SessionManager
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val mD5Password: MD5PasswordUseCase,
    private val sessionManager: SessionManager
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateUser(user: User): Result<User> {
        val currentUser = userRepository.getUserByUserId(user.id.toString())
        return if (user.userName.isBlank()) {
            Result.failure(InvalidUserNameException("invalid username"))
        } else if (user.password.isBlank()) {
            Result.failure(InvalidPasswordException("invalid password"))
        } else if (user.id != sessionManager.getCurrentUser()?.id) {
            Result.failure(UnauthorizedUserException("user unauthorized"))
        } else {
            val updatedUser = currentUser.copy(
                userName = user.userName,
                password = mD5Password.hashPassword(user.password)
            )
            Result.success(userRepository.updateUser(updatedUser))
        }
    }
}