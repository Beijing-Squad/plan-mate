package data.repository.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.UserDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.User

class UserMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : UserDataSource {

    private val collection = database.getCollection<User>("users")

    override  fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override  fun getUserByUserId(userId: String): User {
        TODO("Not yet implemented")
    }

    override  fun updateUser(user: User): User {
        TODO("Not yet implemented")
    }
}