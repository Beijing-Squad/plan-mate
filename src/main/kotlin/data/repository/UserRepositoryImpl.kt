package data.repository

import data.repository.mapper.toUserDto
import data.repository.mapper.toUserEntity
import data.repository.remoteDataSource.RemoteDataSource
import logic.entities.User
import logic.repository.UserRepository

class UserRepositoryImpl(
    private val userMongoDataSource: RemoteDataSource
) : UserRepository {
    override suspend fun getAllUsers(): List<User> {
        return userMongoDataSource.getAllUsers().map { it.toUserEntity() }
    }

    override suspend fun getUserByUserId(userId: String): User {
        return userMongoDataSource.getUserByUserId(userId).toUserEntity()
    }

    override suspend fun updateUser(user: User): User {
        val userDto = user.toUserDto()
        return userMongoDataSource.updateUser(userDto).toUserEntity()
    }

}
