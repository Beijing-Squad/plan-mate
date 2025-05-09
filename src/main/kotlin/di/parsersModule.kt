package di

import data.parser.*
import org.koin.dsl.module

val parsersModule = module {
    single { ProjectCsvParser() }
    single { UserCsvParser() }
    single { TaskCsvParser() }
    single { StateCsvParser() }
    single { AuditCsvParser() }
}
