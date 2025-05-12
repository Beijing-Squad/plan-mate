package logic.repository

import logic.entities.User
import logic.entities.type.UserRole

interface AuthenticationRepository {
    suspend fun registerUser(username: String, password: String, role: UserRole): Boolean
    suspend fun loginUser(username: String, password: String): User
}