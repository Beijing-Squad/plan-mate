package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.local.csvDataSource.MD5HashPasswordImpl
import data.local.csvDataSource.ValidationUserDataSourceImpl
import data.remote.mongoDataSource.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.AuditDataSource
import data.repository.dataSource.ProjectDataSource
import data.repository.dataSource.StatesDataSource
import data.repository.dataSource.TasksDataSource
import data.repository.remoteDataSource.AuthenticationMongoDBDataSource
import data.repository.remoteDataSource.UserMongoDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {

    single<PasswordHashingDataSource> { MD5HashPasswordImpl() }
    single<ValidationUserDataSource> { ValidationUserDataSourceImpl() }

    single<MongoDatabase> {
        MongoConnection.database
    }

    single(named("dbScope")) {
        MongoConnection.dbScope
    }

    single<UserMongoDataSource> {
        UserMongoDataSourceImpl(get(), get(),get())
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