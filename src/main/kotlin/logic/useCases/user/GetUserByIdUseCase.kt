package logic.useCases.user

import logic.entity.User
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getUserByUserId(userId: String): User {
        return userRepository.getUserByUserId(userId = userId)
    }
}
