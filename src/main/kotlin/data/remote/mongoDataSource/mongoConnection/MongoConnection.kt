package data.remote.mongoDataSource.mongoConnection

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object MongoConnection {
    private val client by lazy { createClient() }
    val database by lazy { client.getDatabase("planMate") }
    val dbScope by lazy { CoroutineScope(Dispatchers.IO) }

    private fun createClient(): MongoClient {
        val connectionString = System.getProperty("MONGO_CONNECTION_STRING")
            ?: throw IllegalStateException("MongoDB connection string not set. Please set MONGO_CONNECTION_STRING system property.")

        return MongoClient.Factory.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .build()
        )
    }

}