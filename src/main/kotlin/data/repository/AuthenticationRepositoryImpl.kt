package data.repository

import data.repository.dataSource.AuthenticationDataSource
import logic.entities.User
import logic.entities.UserRole
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val dataSource: AuthenticationDataSource
) : AuthenticationRepository {

    override fun registerUser(username: String, password: String, role: UserRole): Boolean =
        dataSource.saveUser(username, password, role)

    override fun loginUser(username: String, password: String): User =
        dataSource.getAuthenticatedUser(username, password)
}