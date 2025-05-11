package data.utils

import java.io.File
import java.util.*

object MongoConfig {
    private val props: Properties by lazy {
        File("keys.properties").takeIf { it.exists() }
            ?.inputStream()?.use { Properties().apply { load(it) } }
            ?: error("Missing keys.properties")
    }

    val username: String by lazy {
        props.getProperty("MONGO_USERNAME") ?: error("MONGO_USERNAME not found in keys.properties")
    }

    val password: String by lazy {
        props.getProperty("MONGO_PASSWORD") ?: error("MONGO_PASSWORD not found in keys.properties")
    }

    val uri: String by lazy {
        "mongodb+srv://$username:$password@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    }
}