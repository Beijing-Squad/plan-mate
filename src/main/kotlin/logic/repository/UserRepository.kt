package logic.repository

import logic.entities.User

interface UserRepository{
    suspend fun getAllUsers(): List<User>

    suspend fun getUserByUserId(userId: String): User

    suspend fun updateUser(user: User): User
}