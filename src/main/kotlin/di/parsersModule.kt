package di

import data.local.csvDataSource.parser.AuditCsvParser
import data.local.csvDataSource.parser.ProjectCsvParser
import data.local.csvDataSource.parser.StateCsvParser
import data.local.csvDataSource.parser.TaskCsvParser
import data.local.csvDataSource.parser.UserCsvParser
import org.koin.dsl.module

val parsersModule = module {
    single { ProjectCsvParser() }
    single { UserCsvParser() }
    single { TaskCsvParser() }
    single { TaskStateCsvParser() }
    single { AuditCsvParser() }
}
