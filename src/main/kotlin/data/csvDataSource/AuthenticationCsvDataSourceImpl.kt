package data.csvDataSource

import data.parser.CsvPlanMateParser
import data.parser.CsvPlanMateReader
import data.repository.dataSource.AuthenticationDataSource
import logic.entities.User

class AuthenticationCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
) : AuthenticationDataSource {

    override fun login(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}