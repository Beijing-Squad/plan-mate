package data.remote.mongoDataSource.mongoConnection

import kotlinx.coroutines.*
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

object MongoConnection {

    private val props: Properties by lazy {
        Properties().apply {
            File("keys.properties").takeIf { it.exists() }
                ?.inputStream()?.use { load(it) }
                ?: error("❌ Missing keys.properties file")
        }
    }

    private val uri by lazy {
        val user = props.getProperty("MONGO_USERNAME") ?: error("❌ MONGO_USERNAME not found in keys.properties")
        val pass = props.getProperty("MONGO_PASSWORD") ?: error("❌ MONGO_PASSWORD not found in keys.properties")
        "mongodb+srv://$user:$pass@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    }

    val dbScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    val database: CoroutineDatabase? by lazy {
        Logger.getLogger("org.mongodb.driver").level = Level.WARNING

        try {
            println("🔌 Attempting MongoDB connection...")
            val client: CoroutineClient = KMongo.createClient(uri).coroutine
            val db = client.getDatabase("planMate")

            runBlocking {
                db.runCommand<Document>(Document("ping", 1))
            }


            println("✅ MongoDB connection successful.")
            db
        } catch (e: Exception) {
            println("❌ MongoDB connection failed: ${e.message}")
            null
        }
    }
}
