package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.RemoteDataSource
import org.koin.dsl.bind
import org.koin.dsl.module

val mongoModule = module {

    single<MongoDatabase> {
        MongoConnection.database
    }

    single {
        MongoDBDataSourceImpl(get())
    } bind RemoteDataSource::class
}