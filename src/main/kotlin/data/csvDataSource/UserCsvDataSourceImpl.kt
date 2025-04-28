package data.csvDataSource

import data.parser.CsvPlanMateParser
import data.parser.CsvPlanMateReader
import data.repository.dataSource.UserDataSource
import logic.entities.User

class UserCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
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

    override fun updateUser(userId: String): User {
        TODO("Not yet implemented")
    }

}