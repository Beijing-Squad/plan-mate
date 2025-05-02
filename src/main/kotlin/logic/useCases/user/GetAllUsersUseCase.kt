package logic.useCases.user

import logic.entities.User
import logic.repository.UserRepository

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<User>{
        return userRepository.getAllUsers()
    }
}