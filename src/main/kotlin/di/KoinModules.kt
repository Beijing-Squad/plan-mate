package di

import data.parser.*
import org.koin.dsl.module

val csvParsersModule = module {
    single { UserCsvParser() }
    single { TaskCsvParser() }
    single { StateCsvParser() }
    single { AuditCsvParser() }
    single { ProjectCsvParser() }
}