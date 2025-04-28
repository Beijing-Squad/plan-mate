package data.repository

import data.repository.dataSourceAbstraction.UserDataSource
import logic.entities.User
import logic.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository{
    override fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getUserByUserId(userId: String): User {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(userId: String): User {
        TODO("Not yet implemented")
    }

}
