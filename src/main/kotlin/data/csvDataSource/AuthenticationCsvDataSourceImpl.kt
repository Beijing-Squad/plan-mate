package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.AuthenticationDataSource
import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.entities.exceptions.UserNotFoundException

class AuthenticationCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>,
    private val userDataSource: UserDataSource
) : AuthenticationDataSource {

    override fun saveUser(user: User): Boolean {
        return try {
            csvDataSource.appendToFile(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getUserByUsername(username: String): User {
        return userDataSource
            .getAllUsers()
            .find { it.userName == username }
            ?: throw UserNotFoundException("invalid username or password")
    }
}