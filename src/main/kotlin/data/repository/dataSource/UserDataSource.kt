package data.repository.dataSource

import logic.entities.User

interface UserDataSource{

    fun getAllUsers(): List<User>

    fun getUserByUserId(userId: String): User

    fun updateUser(user: User): User
}