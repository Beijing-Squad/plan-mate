package di

import data.local.csvDataSource.LocalDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.local.csvDataSource.parser.*
import data.repository.localDataSource.LocalDataSource
import logic.entity.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {
    // CsvDataSourceImpl
    single(named("projectDataSource")) {
        CsvDataSourceImpl<Project>(
            get(named("projectReader")),
            get(named("projectWriter")),
            get<ProjectCsvParser>()
        )
    }
    single(named("userDataSource")) {
        CsvDataSourceImpl<User>(
            get(named("userReader")),
            get(named("userWriter")),
            get<UserCsvParser>()
        )
    }
    single(named("taskDataSource")) {
        CsvDataSourceImpl<Task>(
            get(named("taskReader")),
            get(named("taskWriter")),
            get<TaskCsvParser>()
        )
    }
    single(named("stateDataSource")) {
        CsvDataSourceImpl<TaskState>(
            get(named("stateReader")),
            get(named("stateWriter")),
            get<TaskStateCsvParser>()
        )
    }
    single(named("auditDataSource")) {
        CsvDataSourceImpl<Audit>(
            get(named("auditReader")),
            get(named("auditWriter")),
            get<AuditCsvParser>()
        )
    }
    single (named("authenticationDataSource")){
        CsvDataSourceImpl(
            get(named("authenticationReader")),
            get(named("authenticationWriter")),
            get<UserCsvParser>()
        )
    }

    // Implementations
    single<LocalDataSource> {
        LocalDataSourceImpl(
            get(named("projectDataSource")),
            get(named("auditDataSource")),
            get(named("taskDataSource")),
            get(named("stateDataSource")),
            get(named("userDataSource"))
        )
    } bind LocalDataSource::class
}