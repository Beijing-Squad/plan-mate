package data.repository.dataSource

import logic.entities.User

interface AuthenticationDataSource{

    fun login(email: String, password: String): User

}