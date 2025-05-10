package di

import data.common.MD5HashPasswordImpl
import data.common.PasswordHashingDataSource
import data.local.csvDataSource.*
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.local.csvDataSource.parser.*
import data.repository.ValidationUserDataSource
import data.repository.dataSource.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {
    // CsvDataSourceImpl
    single(named("projectDataSource")) {
        CsvDataSourceImpl(
            get(named("projectReader")),
            get(named("projectWriter")),
            get<ProjectCsvParser>()
        )
    }
    single(named("userDataSource")) {
        CsvDataSourceImpl(
            get(named("userReader")),
            get(named("userWriter")),
            get<UserCsvParser>()
        )
    }
    single(named("taskDataSource")) {
        CsvDataSourceImpl(
            get(named("taskReader")),
            get(named("taskWriter")),
            get<TaskCsvParser>()
        )
    }
    single(named("stateDataSource")) {
        CsvDataSourceImpl(
            get(named("stateReader")),
            get(named("stateWriter")),
            get<TaskStateCsvParser>()
        )
    }
    single(named("auditDataSource")) {
        CsvDataSourceImpl(
            get(named("auditReader")),
            get(named("auditWriter")),
            get<AuditCsvParser>()
        )
    }
    single(named("authenticationDataSource")) {
        CsvDataSourceImpl(
            get(named("authenticationReader")),
            get(named("authenticationWriter")),
            get<UserCsvParser>()
        )
    }
    single<PasswordHashingDataSource> { MD5HashPasswordImpl() }
    single<ValidationUserDataSource> { ValidationUserDataSourceImpl() }

    // Implementations
    single { ProjectCsvDataSourceImpl(get(named("projectDataSource"))) } bind ProjectDataSource::class
    single { UserCsvDataSourceImpl(get(named("userDataSource")), get(), get()) } bind UserDataSource::class
    single { TasksCsvDataSourceImpl(get(named("taskDataSource"))) } bind TasksDataSource::class
    single { TaskStatesCsvDataSourceImpl(get(named("stateDataSource"))) } bind StatesDataSource::class
    single { AuditCsvDataSourceImpl(get(named("auditDataSource"))) } bind AuditDataSource::class
    single {
        AuthenticationCsvDataSourceImpl(get(named("authenticationDataSource")), get(), get(), get())
    } bind AuthenticationDataSource::class
}