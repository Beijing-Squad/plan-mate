package data.remote.mongoDataSource

import com.mongodb.MongoTimeoutException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.UserDTO
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.UserMongoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import logic.entities.exceptions.UserNotFoundException

class UserMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : UserMongoDataSource {

    private val collection = database.getCollection<UserDTO>(USER_COLLECTION)

    override suspend fun getAllUsers(): List<UserDTO> {
        return withContext(Dispatchers.IO) {
            try {
                collection.find().toList()
            } catch (e: MongoTimeoutException) {
                println("Database connection failed: ${e.message}")
                emptyList()
            }
        }

    }

    override suspend fun getUserByUserId(userId: String): UserDTO {
        return getAllUsers()
            .find { it.id == userId }
            ?: throw UserNotFoundException()
    }

    override suspend fun updateUser(user: UserDTO): UserDTO {
        val filters = Filters.eq(UserDTO::id.name, user.id)
        val existingUser = collection.find(filters).firstOrNull() ?: throw UserNotFoundException()

        val updates = buildList {
            if (user.userName.isNotBlank() && user.userName != existingUser.userName) {
                add(Updates.set(UserDTO::userName.name, user.userName))
            }
            if (user.password.isNotBlank() && user.password != existingUser.password) {
                add(Updates.set(UserDTO::password.name, user.password))
            }
        }

        if (updates.isNotEmpty()) {
            val result = collection.updateOne(filters, Updates.combine(updates))
            require(result.matchedCount.toInt() != 0) { throw UserNotFoundException() }
        }

        return collection.find(filters).firstOrNull() ?: throw UserNotFoundException()
    }

    private companion object {
        const val USER_COLLECTION = "users"
    }
}