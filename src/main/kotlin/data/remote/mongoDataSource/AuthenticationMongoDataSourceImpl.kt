package data.remote.mongoDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.UserDTO
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.AuthenticationMongoDBDataSource
import kotlinx.coroutines.flow.firstOrNull
import logic.entities.exceptions.InvalidLoginException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthenticationMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database
) : AuthenticationMongoDBDataSource {
    private val collection = database.getCollection<UserDTO>("users")

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveUser(username: String, password: String, role: String): Boolean {
        val newUser = UserDTO(
            id = Uuid.random().toString(),
            userName = username,
            password = password,
            role = role
        )
        val result = collection.insertOne(newUser)
        return result.wasAcknowledged()
    }

    override suspend fun getAuthenticatedUser(username: String, password: String): UserDTO {
        val query = Filters.and(eq("userName", username), eq("password", password))
        return collection.find(query).firstOrNull() ?: throw InvalidLoginException()
    }
}