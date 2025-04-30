package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.entities.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class UserCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>
) : UserDataSource {
    private val users = csvDataSource.loadAll().toMutableList()

    override fun getAllUsers(): List<User> {
        return users.toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserByUserId(userId: String): User {
        return getAllUsers()
            .find { it.id.toString() == userId }
            ?: throw UserNotFoundException("user not found")

    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateUser(user: User): User {
        val currentUser = getUserByUserId(user.id.toString())
        val userUpdated = currentUser
            .copy(userName = user.userName, password = user.password)
        users[users.indexOf(currentUser)] = userUpdated
        csvDataSource.update(users)
        return currentUser
    }

}