package data.repository.remoteDataSource

import data.dto.UserDTO

interface UserMongoDataSource {

    suspend fun getAllUsers(): List<UserDTO>

    suspend fun getUserByUserId(userId: String): UserDTO

    suspend fun updateUser(user: UserDTO): UserDTO
}