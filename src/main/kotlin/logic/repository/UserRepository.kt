package logic.repository

import logic.entities.User

interface UserRepository{
    fun getAllUsers(): List<User>

    fun getUserByUserId(userId: String): User

    fun updateUser(user: User): User
}