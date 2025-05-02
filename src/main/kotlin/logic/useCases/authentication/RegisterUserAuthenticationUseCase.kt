package logic.useCases.authentication

import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.UserExistsException
import logic.repository.AuthenticationRepository
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RegisterUserAuthenticationUseCase(
    private val repository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val md5Password: MD5PasswordUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun execute(username: String, password: String, role: UserRole): Boolean {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(password.isNotBlank()) { "Password cannot be black" }
        val hashedPassword = md5Password.hashPassword(password)
        val user = User(Uuid.random(), username, hashedPassword, role)
        val existingUser = userRepository.getAllUsers().find { it.userName == username }

        return if (existingUser == null) {
            repository.registerUser(user)
        } else {
            throw UserExistsException("User already exists")
        }
    }
}