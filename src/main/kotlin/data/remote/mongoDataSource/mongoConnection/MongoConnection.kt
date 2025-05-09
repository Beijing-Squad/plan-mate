package data.remote.mongoDataSource.mongoConnection

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers import java.io.File
import java.util.Properties

object MongoConnection {
    private val client by lazy { createClient() }
    val database by lazy { client.getDatabase("planMate") }
    val dbScope by lazy { CoroutineScope(Dispatchers.IO) }

    private fun createClient(): MongoClient {
        val props = Properties().apply {
            File("keys.properties").takeIf { it.exists() }
                ?.inputStream()?.use { load(it) }
                ?: error("Missing keys.properties")
        }
        val user = props.getProperty("MONGO_USERNAME") ?: error("MONGO_USERNAME not found in keys.properties")
        val pass = props.getProperty("MONGO_PASSWORD") ?: error("MONGO_PASSWORD not found in keys.properties")

        val uri = "mongodb+srv://$user:$pass@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

        return MongoClient.Factory.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(uri))
                .build()
        )
    }
}