package data.local.csvDataSource

import data.common.hashPassword
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.localDataSource.AuthenticationDataSource
import data.repository.localDataSource.UserDataSource
import logic.entities.User
import logic.entities.UserRole
import logic.exceptions.InvalidPasswordException
import logic.exceptions.UserExistsException
import logic.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthenticationCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>,
    private val userDataSource: UserDataSource
) : AuthenticationDataSource {

    override fun saveUser(username: String, password: String, role: UserRole): Boolean {
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
}