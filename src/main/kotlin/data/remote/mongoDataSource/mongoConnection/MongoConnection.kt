package data.remote.mongoDataSource.mongoConnection

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.utils.MongoClientProvider

object MongoConnection {
    val database: MongoDatabase by lazy {
        MongoClientProvider.client.getDatabase("planMate")
    }
}