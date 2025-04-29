package di

import data.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single { ProjectsRepositoryImpl(get()) }
    single { UserRepositoryImpl(get()) }
    single { TasksRepositoryImpl(get()) }
    single { AuditRepositoryImpl(get()) }
    single { StatesRepositoryImpl(get()) }
}
