package logic.useCases.user

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.UserRepository
import logic.useCases.user.cryptography.PasswordEncryption
import kotlin.uuid.ExperimentalUuidApi

class AddUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncryption: PasswordEncryption
) {
    @OptIn(ExperimentalUuidApi::class)
    fun addUser(user: User): Result<Boolean> {
        return if (isUsernameInvalid(user.userName)) {
            Result.failure(InvalidUserNameException("invalid username"))
        } else if (isPasswordInvalid(user.password)) {
            Result.failure(InvalidPasswordException("invalid password"))
        } else {
            userRepository.addUser(
                user
                    .copy(
                        password = passwordEncryption.encryptionPassword(user.password)
                    )
            )
            Result.success(true)
        }

    }

    private fun isUsernameInvalid(userName: String): Boolean {
        return !userName.matches(Regex(USER_PATTERN)) ||
                userName.length < MAX_USER_NAME_LENGTH
    }

    private fun isPasswordInvalid(password: String): Boolean {
        return password.length < MAX_PASSWORD_LENGTH
    }

    private companion object {
        const val MAX_USER_NAME_LENGTH = 3
        const val MAX_PASSWORD_LENGTH = 8
        const val USER_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$"
    }
}