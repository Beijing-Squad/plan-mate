package di

import data.local.csvDataSource.AuthenticationCsvDataSourceImpl
import data.local.csvDataSource.ProjectCsvDataSourceImpl
import data.local.csvDataSource.UserCsvDataSourceImpl
import data.local.csvDataSource.ValidationUserDataSourceImpl
import data.local.csvDataSource.csv.CsvDataSourceImpl
import data.local.csvDataSource.parser.AuditCsvParser
import data.local.csvDataSource.parser.ProjectCsvParser
import data.local.csvDataSource.parser.TaskCsvParser
import data.local.csvDataSource.parser.TaskStateCsvParser
import data.local.csvDataSource.TaskStatesCsvDataSourceImpl
import data.local.csvDataSource.TasksCsvDataSourceImpl
import data.local.csvDataSource.AuditCsvDataSourceImpl
import data.local.csvDataSource.MD5HashPasswordImpl
import data.local.csvDataSource.parser.UserCsvParser
import data.repository.PasswordHashingDataSource
import data.repository.ValidationUserDataSource
import data.repository.dataSource.*
import logic.entities.*
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
    single<PasswordHashingDataSource> { MD5HashPasswordImpl() }
    single<ValidationUserDataSource> { ValidationUserDataSourceImpl() }

    // Implementations
    single { ProjectCsvDataSourceImpl(get(named("projectDataSource"))) } bind ProjectDataSource::class
    single { UserCsvDataSourceImpl(get(named("userDataSource")),get(),get()) } bind UserDataSource::class
    single { TasksCsvDataSourceImpl(get(named("taskDataSource"))) } bind TasksDataSource::class
    single { TaskStatesCsvDataSourceImpl(get(named("stateDataSource"))) } bind StatesDataSource::class
    single { AuditCsvDataSourceImpl(get(named("auditDataSource"))) } bind AuditDataSource::class
    single { AuthenticationCsvDataSourceImpl(get(named("authenticationDataSource")),get(),get(),get())
    }bind AuthenticationDataSource::class
}