package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.common.MD5HashPasswordImpl
import data.remote.mongoDataSource.MongoDBDataSourceImpl
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.common.PasswordHashingDataSource
import data.local.csvDataSource.ValidationUserDataSourceImpl
import data.repository.ValidationUserDataSource
import data.repository.remoteDataSource.MongoDBDataSource
import org.koin.dsl.bind
import org.koin.dsl.module

val mongoModule = module {

    single<MongoDatabase> {
        MongoConnection.database
    }

    single<PasswordHashingDataSource> { MD5HashPasswordImpl() }
    single<ValidationUserDataSource> { ValidationUserDataSourceImpl() }

    single {
        MongoDBDataSourceImpl(get(), get())
    } bind MongoDBDataSource::class

}