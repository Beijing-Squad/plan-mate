package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.*
import data.repository.mongoDataSource.UserMongoDataSourceImpl
import data.repository.remoteDataSource.AuthenticationMongoDBDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {

    single<MongoDatabase> {
        MongoConnection.database
    }

    single(named("dbScope")) {
        MongoConnection.dbScope
    }

    single<UserDataSource> {
        UserMongoDataSourceImpl(get(), get(named("dbScope")))
    }

    single<TasksDataSource> {
        TaskMongoDataSourceImpl(get(), get(named("dbScope")))
    }

    single<ProjectDataSource> {
        ProjectMongoDataSourceImpl(get(), get(named("dbScope")))
    }

    single<StatesDataSource> {
        StateMongoDataSourceImpl(get(), get(named("dbScope")))
    }

    single<AuditDataSource> {
        AuditMongoDataSourceImpl(get(), get(named("dbScope")))
    }

    single<AuthenticationMongoDBDataSource> {
        AuthenticationMongoDataSourceImpl(get())
    }
}