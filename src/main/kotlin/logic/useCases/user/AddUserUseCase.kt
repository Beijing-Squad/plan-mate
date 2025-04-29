package logic.useCases.user

import logic.entities.User
import logic.repository.UserRepository

class AddUserUseCase(
    private val userRepository: UserRepository
) {
    fun addUserUseCase(user: User): Boolean{
         return false
    }
}