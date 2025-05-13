package logic.repository

import logic.entity.User
import logic.entity.type.UserRole

interface AuthenticationRepository {
    suspend fun registerUser(username: String, password: String, role: UserRole): Boolean
    suspend fun loginUser(username: String, password: String): User
}