package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.UserDataSource
import logic.entities.User
import logic.entities.exceptions.UserNotFoundException
import kotlin.uuid.ExperimentalUuidApi

class UserCsvDataSourceImpl(
    private val csvDataSource: CsvDataSourceImpl<User>,
    private val validationUserDataSource: ValidationUserDataSource,
    private val mD5HashPasswordImpl: PasswordHashingDataSource
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
        validationUserDataSource.validateUsername(user.userName)
        validationUserDataSource.validatePassword(user.password)
        val currentUser = getUserByUserId(user.id.toString())
        val passwordToUse = if (user.password != currentUser.password) {
            mD5HashPasswordImpl.hashPassword(user.password)
        } else {
            currentUser.password
        }
        val updatedUser = currentUser
            .copy(userName = user.userName, password = passwordToUse)
        csvDataSource.updateItem(updatedUser)
        return updatedUser
    }
}
