package di

import data.csvDataSource.*
import data.csvDataSource.csv.CsvDataSourceImpl
import data.parser.AuditCsvParser
import data.parser.ProjectCsvParser
import data.parser.StateCsvParser
import data.parser.TaskCsvParser
import data.parser.UserCsvParser
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
        CsvDataSourceImpl<State>(
            get(named("stateReader")),
            get(named("stateWriter")),
            get<StateCsvParser>()
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
    single { ProjectCsvDataSourceImpl(get(named("projectDataSource"))) } bind ProjectDataSource::class
    single { UserCsvDataSourceImpl(get(named("userDataSource")),get()) } bind UserDataSource::class
    single { TasksCsvDataSourceImpl(get(named("taskDataSource"))) } bind TasksDataSource::class
    single { StatesCsvDataSourceImpl(get(named("stateDataSource"))) } bind StatesDataSource::class
    single { AuditCsvDataSourceImpl(get(named("auditDataSource"))) } bind AuditDataSource::class
    single { AuthenticationCsvDataSourceImpl(get(named("authenticationDataSource")),get())
    }bind AuthenticationDataSource::class
}
