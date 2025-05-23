package data.repository

import data.repository.mapper.toUserEntity
import data.repository.remoteDataSource.MongoDBDataSource
import logic.entities.User
import logic.entities.UserRole
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val dataSource: MongoDBDataSource
) : AuthenticationRepository {

    override suspend fun registerUser(username: String, password: String, role: UserRole): Boolean =
        dataSource.saveUser(username, password, role.name)

    override suspend fun loginUser(username: String, password: String): User =
        toUserEntity(dataSource.getAuthenticatedUser(username, password))
}