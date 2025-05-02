package data.repository

import data.repository.dataSource.AuthenticationDataSource
import logic.entities.User
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val dataSource: AuthenticationDataSource
) : AuthenticationRepository {

    override fun registerUser(user: User): Boolean = dataSource.saveUser(user)
    override fun loginUser(username: String, password: String): User = dataSource.getUserByUsername(username)

}