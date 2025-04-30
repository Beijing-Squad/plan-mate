package logic.useCases.user

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.UserRepository
import logic.useCases.user.cryptography.PasswordEncryption
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncryption: PasswordEncryption,
    private val validateUser: ValidateUserUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateUser(user: User): Result<User> {
        val currentUser = userRepository.getUserByUserId(user.id.toString())
        return if (validateUser.validateUserName(user.userName)) {
            Result.failure(InvalidUserNameException("invalid username"))
        } else if (validateUser.validatePassword(user.password)) {
            Result.failure(InvalidPasswordException("invalid password"))
        } else {
            val updatedUser = currentUser.copy(
                userName = user.userName,
                password = passwordEncryption.encryptionPassword(user.password)
            )
            Result.success(userRepository.updateUser(updatedUser))
        }

    }
}