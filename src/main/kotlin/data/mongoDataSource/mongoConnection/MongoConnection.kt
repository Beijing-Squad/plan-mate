package data.mongoDataSource.mongoConnection

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object MongoConnection {
    private const val CONNECTION_STRING = "mongodb+srv://radwamohamed5033:itGzVSNEk5JDnN95@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    val client = MongoClient.Factory.create(
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(CONNECTION_STRING))
            .build()
    )

    val database = client.getDatabase("planMate")

    val dbScope = CoroutineScope(Dispatchers.IO)

}