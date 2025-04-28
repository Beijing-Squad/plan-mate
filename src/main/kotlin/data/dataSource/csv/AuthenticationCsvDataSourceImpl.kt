package data.dataSource.csv

import data.dataSource.csv.utils.CsvPlanMateParser
import data.dataSource.csv.utils.CsvPlanMateReader
import data.repository.dataSourceAbstraction.AuthenticationDataSource
import logic.entities.User

class AuthenticationCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
) : AuthenticationDataSource {

    override fun login(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}