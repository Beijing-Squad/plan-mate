package logic.useCases.authentication

import logic.entities.User
import logic.repository.AuthenticationRepository

class LoginUserAuthenticationUseCase(
    private val repository: AuthenticationRepository,
    private val sessionManagerUseCase: SessionManagerUseCase
) {
    fun execute(username: String, password: String): User {
        val user = repository.loginUser(username, password)
        sessionManagerUseCase.setCurrentUser(user)
        return user
    }
}