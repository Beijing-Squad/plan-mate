package data.repository.localDataSource

import logic.entities.User
import logic.entities.type.UserRole

interface AuthenticationDataSource {
    fun saveUser(username: String, password: String, role: UserRole): Boolean
    fun getAuthenticatedUser(username: String, password: String): User
}