package logic.useCases.user

import logic.entity.User
import logic.repository.UserRepository

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun getAllUsers(): List<User>{
        return userRepository.getAllUsers()
    }
}