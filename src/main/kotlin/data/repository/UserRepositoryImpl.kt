package data.repository

import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository{
    override fun getAllUsers(): List<User> {
        return userDataSource.getAllUsers()
    }

    override fun getUserByUserId(userId: String): User {
        return userDataSource.getUserByUserId(userId)
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User): User {
        return userDataSource.updateUser(user)
    }

}
