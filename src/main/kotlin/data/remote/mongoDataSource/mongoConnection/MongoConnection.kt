package data.remote.mongoDataSource.mongoConnection


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.io.File
import java.util.*

object MongoConnection {
    private val props: Properties by lazy {
        Properties().apply {
            File("keys.properties").takeIf { it.exists() }
                ?.inputStream()?.use { load(it) }
                ?: error("Missing keys.properties")
        }
    }

    private val uri by lazy {
        val user = props.getProperty("MONGO_USERNAME") ?: error("MONGO_USERNAME not found in keys.properties")
        val pass = props.getProperty("MONGO_PASSWORD") ?: error("MONGO_PASSWORD not found in keys.properties")
        "mongodb+srv://$user:$pass@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    }

    private val client: CoroutineClient by lazy {
        KMongo.createClient(uri).coroutine
    }

    val database: CoroutineDatabase by lazy {
        client.getDatabase("planMate")
    }

    val dbScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}
