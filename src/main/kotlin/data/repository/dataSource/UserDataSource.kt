package data.repository.dataSource

import logic.entities.User

interface UserDataSource{

    fun getAllUsers(): List<User>

    fun getUserByUserId(userId: String): User

    fun addUser(user: User)

    fun updateUser(userId: String): User
}