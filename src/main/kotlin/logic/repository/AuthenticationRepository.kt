package logic.repository

import logic.entities.User
import logic.entities.UserRole

interface AuthenticationRepository {
    fun registerUser(username: String, password: String, role: UserRole): Boolean
    fun loginUser(username: String, password: String): User
}