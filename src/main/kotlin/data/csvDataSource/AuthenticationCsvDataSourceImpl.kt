package data.csvDataSource

import data.repository.dataSource.AuthenticationDataSource
import logic.entities.User

class AuthenticationCsvDataSourceImpl(

) : AuthenticationDataSource {

    override fun login(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}