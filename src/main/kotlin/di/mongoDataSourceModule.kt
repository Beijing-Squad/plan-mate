package di

import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.remoteDataSource.MongoDBDataSource
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val mongoModule = module {

    single<CoroutineDatabase> {
        MongoConnection.database!!
    }

    single<MongoDBDataSource> {
        MongoDBDataSourceImpl(get())
    }

}