package logic.useCases.user

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UnauthorizedUserException
import logic.repository.UserRepository
import logic.useCases.authentication.SessionManagerUseCase
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val sessionManagerUseCase: SessionManagerUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateUser(user: User): Result<User> {
        val currentUser = userRepository.getUserByUserId(user.id.toString())
        return if (user.userName.isBlank()) {
            Result.failure(InvalidUserNameException("invalid username"))
        } else if (user.password.isBlank()) {
            Result.failure(InvalidPasswordException("invalid password"))
        } else if (user.id != sessionManagerUseCase.getCurrentUser()?.id) {
            Result.failure(UnauthorizedUserException("user unauthorized"))
        } else {
            val updatedUser = currentUser.copy(
                userName = user.userName,
                password = user.password
            )
            Result.success(userRepository.updateUser(updatedUser))
        }
    }
}