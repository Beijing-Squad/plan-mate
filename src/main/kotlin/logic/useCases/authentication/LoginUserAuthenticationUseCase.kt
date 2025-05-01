package logic.useCases.authentication

import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.AuthenticationRepository
import java.security.MessageDigest

class LoginUserAuthenticationUseCase(
    private val repository: AuthenticationRepository
) {
    fun execute(username: String, password: String): Boolean {
        require(username.isNotBlank()) { throw InvalidUserNameException("Invalid username") }
        require(password.isNotBlank()) { throw InvalidPasswordException("Invalid password") }

        val user = repository.loginUser(username,password)
            ?: throw InvalidUserNameException("Invalid username")

        val hashedInputPassword = hashPassword(password)
        if (user.password != hashedInputPassword) {
            throw InvalidPasswordException("Invalid password")
        }
        return true
    }

    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}