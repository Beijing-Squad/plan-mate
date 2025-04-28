package data.repository

import data.repository.dataSource.AuthenticationDataSource
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override fun login(username: String, password: String): Boolean {
        TODO("Not yet implemented")
    }
}