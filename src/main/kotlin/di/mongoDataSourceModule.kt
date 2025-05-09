package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.MongoDBDataSource
import org.koin.dsl.module

val mongoModule = module {

    single<MongoDatabase> {
        MongoConnection.database
    }

    single<MongoDBDataSource> {
        MongoDBDataSourceImpl(get())
    }

}