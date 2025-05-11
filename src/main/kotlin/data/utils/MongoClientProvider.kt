package data.utils

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient

object MongoClientProvider {
    val client: MongoClient by lazy {
        MongoClient.Factory.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(MongoConfig.uri))
                .build()
        )
    }
}