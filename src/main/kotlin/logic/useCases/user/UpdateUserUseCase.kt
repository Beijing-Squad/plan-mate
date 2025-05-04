package logic.useCases.user

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val validationUserUseCase: ValidationUserUseCase,
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateUser(user: User): User {
        require(!validationUserUseCase.isUserNameBlank(user.userName)) {
            throw InvalidUserNameException("invalid username")
        }
        require(!validationUserUseCase.isPasswordBlack(user.password)) {
            throw InvalidPasswordException("invalid password")
        }
        return userRepository.updateUser(user)
    }
}