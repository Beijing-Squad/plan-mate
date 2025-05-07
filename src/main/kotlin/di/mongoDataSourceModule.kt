package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.remote.mongoDataSource.*
import data.remote.mongoDataSource.mongoConnection.MongoConnection
import data.repository.dataSource.*
import data.repository.mongoDataSource.UserMongoDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {

    single<MongoDatabase> {
        MongoConnection.database
    }

    // Define coroutine scope for database operations
    single(named("dbScope")) {
        MongoConnection.dbScope
    }

    // Data source implementations
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

    single<AuthenticationDataSource> {
        AuthenticationMongoDataSourceImpl(get(), get(named("dbScope")))
    }
}