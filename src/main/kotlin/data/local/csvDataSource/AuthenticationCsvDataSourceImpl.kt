package data.local.csvDataSource

import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.common.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.AuthenticationDataSource
import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.entities.UserRole
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.UserExistsException
import logic.entities.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthenticationCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>,
    private val userDataSource: UserDataSource,
    private val validationUserDataSource: ValidationUserDataSource,
    private val mD5HashPasswordImpl: PasswordHashingDataSource
) : AuthenticationDataSource {

    override fun saveUser(username: String, password: String, role: UserRole): Boolean {
        validationUserDataSource.validateUsername(username)
        validationUserDataSource.validatePassword(password)

        val existingUser = userDataSource.getAllUsers()
            .find { it.userName == username }
        if (existingUser != null) {
            throw UserExistsException("User already exists")
        }
        val hashedPassword = mD5HashPasswordImpl.hashPassword(password)

        return try {
            val user = createUser(username, hashedPassword, role)
            csvDataSource.appendToFile(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getAuthenticatedUser(username: String, password: String): User {
        validationUserDataSource.validateUsername(username)
        validationUserDataSource.validatePassword(password)

        val user = userDataSource
            .getAllUsers()
            .find { it.userName == username }
            ?: throw UserNotFoundException("Invalid username or password")

        val hashedInputPassword = mD5HashPasswordImpl.hashPassword(password)
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