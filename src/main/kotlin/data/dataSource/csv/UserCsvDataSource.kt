package data.dataSource.csv

import data.repository.dataSourceAbstraction.UserDataSource
import logic.entities.User
import java.io.File

class UserCsvDataSource(
    private val file: File
): UserDataSource{

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