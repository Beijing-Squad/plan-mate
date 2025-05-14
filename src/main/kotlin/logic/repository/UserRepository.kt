package logic.repository

import logic.entity.User

interface UserRepository{
    suspend fun getAllUsers(): List<User>

    suspend fun getUserByUserId(userId: String): User

    suspend fun updateUser(user: User): User
}