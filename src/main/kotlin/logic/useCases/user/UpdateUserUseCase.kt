package logic.useCases.user

import logic.entity.User
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun updateUser(user: User): User {
        return userRepository.updateUser(user)
    }
}