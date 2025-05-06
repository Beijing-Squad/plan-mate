package data.mongoDataSource.mongoConnection

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object MongoConnection {
    private val CONNECTION_STRING: String = System.getProperty("MONGO_CONNECTION_STRING")

    val client = MongoClient.Factory.create(
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(CONNECTION_STRING))
            .build()
    )

    val database = client.getDatabase("planMate")

    val dbScope = CoroutineScope(Dispatchers.IO)

}