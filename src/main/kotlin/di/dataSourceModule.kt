package di

import data.csvDataSource.*
import data.csvDataSource.csv.CsvDataSourceImpl
import logic.entities.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    // CsvDataSourceImpl
    single(named("projectDataSource")) {
        CsvDataSourceImpl<Project>(
            get(named("projectReader")),
            get(named("projectWriter")),
            get()
        )
    }
    single(named("userDataSource")) {
        CsvDataSourceImpl<User>(
            get(named("userReader")),
            get(named("userWriter")),
            get()
        )
    }
    single(named("taskDataSource")) {
        CsvDataSourceImpl<Task>(
            get(named("taskReader")),
            get(named("taskWriter")),
            get()
        )
    }
    single(named("stateDataSource")) {
        CsvDataSourceImpl<State>(
            get(named("stateReader")),
            get(named("stateWriter")),
            get()
        )
    }
    single(named("auditDataSource")) {
        CsvDataSourceImpl<Audit>(
            get(named("auditReader")),
            get(named("auditWriter")),
            get()
        )
    }

    // Implementations
    single { ProjectCsvDataSourceImpl(get(named("projectDataSource"))) }
    single { UserCsvDataSourceImpl(get(named("userDataSource"))) }
    single { TasksCsvDataSourceImpl(get(named("taskDataSource"))) }
    single { StatesCsvDataSourceImpl(get(named("stateDataSource"))) }
    single { AuditCsvDataSourceImpl(get(named("auditDataSource"))) }
}
