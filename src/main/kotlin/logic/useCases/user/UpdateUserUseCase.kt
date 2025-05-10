package logic.useCases.user

import logic.entities.User
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateUserUseCase(
    private val userRepository: UserRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateUser(user: User): User {
        return userRepository.updateUser(user)
    }
}