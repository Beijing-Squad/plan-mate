package data.local.csvDataSource

import data.common.hashPassword
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.repository.localDataSource.UserDataSource
import logic.entities.User
import logic.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class UserCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>
) : UserDataSource {

    override fun getAllUsers(): List<User> {
        return csvDataSource
            .loadAllDataFromFile()
            .toMutableList()
            .toList()
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserByUserId(userId: String): User {
        return getAllUsers()
            .find { it.id.toString() == userId }
            ?: throw UserNotFoundException()

    }

    @OptIn(ExperimentalUuidApi::class)
    override fun updateUser(user: User): User {
        val currentUser = getUserByUserId(user.id.toString())
        val passwordToUse = if (user.password != currentUser.password) {
            hashPassword(user.password)
        } else {
            currentUser.password
        }
        val updatedUser = currentUser
            .copy(userName = user.userName, password = passwordToUse)
        csvDataSource.updateItem(updatedUser)
        return updatedUser
    }
}
