package data.repository.remoteDataSource

import data.dto.UserDTO

interface AuthenticationMongoDBDataSource {
    suspend fun saveUser(username: String, password: String, role: String): Boolean
    suspend fun getAuthenticatedUser(username: String, password: String): UserDTO
}