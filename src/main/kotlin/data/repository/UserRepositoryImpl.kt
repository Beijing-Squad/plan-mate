package data.repository

import data.repository.mapper.toUserDto
import data.repository.mapper.toUserEntity
import data.repository.remoteDataSource.UserMongoDataSource
import logic.entities.User
import logic.repository.UserRepository

class UserRepositoryImpl(
    private val userMongoDataSource: UserMongoDataSource
): UserRepository{
    override suspend fun getAllUsers(): List<User> {
        return userMongoDataSource.getAllUsers().map { toUserEntity(it) }
    }

    override suspend fun getUserByUserId(userId: String): User {
        return toUserEntity(userMongoDataSource.getUserByUserId(userId))
    }

    override suspend fun updateUser(user: User): User {
        val userDto = toUserDto(user)
        return toUserEntity(userMongoDataSource.updateUser(userDto))
    }

}
