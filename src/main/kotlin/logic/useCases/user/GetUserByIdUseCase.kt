package logic.useCases.user

import logic.entities.User
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun getUserByUserId(userId: String): User {
        return userRepository.getUserByUserId(userId = userId)
    }
}
