package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.UserDataSource
import logic.entities.User

class UserCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>
): UserDataSource {

    override fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getUserByUserId(userId: String): User {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User): User {
        TODO("Not yet implemented")
    }

}