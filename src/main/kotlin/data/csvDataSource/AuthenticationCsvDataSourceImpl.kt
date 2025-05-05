package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.AuthenticationDataSource
import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException
import logic.entities.exceptions.UserExistsException
import logic.entities.exceptions.UserNotFoundException
import java.security.MessageDigest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthenticationCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>,
    private val userDataSource: UserDataSource
) : AuthenticationDataSource {

    override fun saveUser(username: String, password: String, role: UserRole): Boolean {
        validateUsername(username)
        validatePassword(password)

        val existingUser = userDataSource.getAllUsers()
            .find { it.userName == username }
        if (existingUser != null) {
            throw UserExistsException("User already exists")
        }
        val hashedPassword = hashPassword(password)

        return try {
            val user = createUser(username, hashedPassword, role)
            csvDataSource.appendToFile(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAuthenticatedUser(username: String, password: String): User {
        validateUsername(username)
        validatePassword(password)

        val user = userDataSource
            .getAllUsers()
            .find { it.userName == username }
            ?: throw UserNotFoundException("Invalid username or password")

        val hashedInputPassword = hashPassword(password)
        if (user.password != hashedInputPassword) {
            throw InvalidPasswordException("Invalid password")
        }
        return user
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun createUser(username: String, hashedPassword: String, role: UserRole): User {
        return User(Uuid.random(), username, hashedPassword, role)
    }

    fun validateUsername(username: String) {
        if (username.isBlank()) {
            throw InvalidUserNameException("Username cannot be blank")
        }
    }

    fun validatePassword(password: String) {
        if (password.isBlank()) {
            throw InvalidPasswordException("Password cannot be blank")
        }
    }

    fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}