package data.remote.mongoDataSource

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.AuthenticationDataSource
import kotlinx.coroutines.CoroutineScope
import logic.entities.User
import logic.entities.UserRole

class AuthenticationMongoDataSourceImpl(
    private val database: MongoDatabase = MongoConnection.database,
    private val dbScope: CoroutineScope = MongoConnection.dbScope
) : AuthenticationDataSource {
    override fun saveUser(
        username: String,
        password: String,
        role: UserRole
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAuthenticatedUser(username: String, password: String): User {
        TODO("Not yet implemented")
    }


}