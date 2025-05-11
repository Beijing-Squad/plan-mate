package logic.useCases.authentication

import logic.entities.type.UserRole
import logic.repository.AuthenticationRepository

class RegisterUserAuthenticationUseCase(
    private val repository: AuthenticationRepository
) {
    suspend fun execute(username: String, password: String, role: UserRole): Boolean {
        return repository.registerUser(username, password, role)
    }
}