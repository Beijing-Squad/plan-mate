package data.repository.dataSourceAbstraction

import logic.entities.User

interface AuthenticationDataSource{

    fun login(email: String, password: String): User

}