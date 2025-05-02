package logic.useCases.authentication

import logic.entities.User
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.repository.AuthenticationRepository

class LoginUserAuthenticationUseCase(
    private val repository: AuthenticationRepository,
    private val md5Password: MD5PasswordUseCase
) {
    fun execute(username: String, password: String): User {
        require(username.isNotBlank()) { throw InvalidUserNameException(USERNAME_ERROR) }
        require(password.isNotBlank()) { throw InvalidPasswordException(PASSWORD_ERROR) }

        val user = repository.loginUser(username, password)
            ?: throw InvalidUserNameException(USERNAME_ERROR)

        val hashedInputPassword = md5Password.hashPassword(password)
        if (user.password != hashedInputPassword) {
            throw InvalidPasswordException(PASSWORD_ERROR)
        }
        return user
    }

    private companion object {
        const val USERNAME_ERROR = "Invalid username"
        const val PASSWORD_ERROR = "Invalid password"
    }
}