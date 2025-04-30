package logic.useCases.authentication

import logic.entities.User
import logic.repository.AuthenticationRepository
import java.security.MessageDigest

class LoginUserAuthenticationUseCase(
    private val repository: AuthenticationRepository
) {
    fun execute(username: String, password: String): Boolean {
        val user = repository.loginUser(username,password) ?: return false
        val hashedPassword = hashPassword(password)
        return user.password == hashedPassword
    }

    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}