package data.repository.dataSource

import logic.entities.User

interface AuthenticationDataSource{

    fun saveUser(user: User): Boolean
    fun getUserByUsername(username: String): User
}