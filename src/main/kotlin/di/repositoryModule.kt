package di

import data.repository.*
import logic.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<TasksRepository> { TasksRepositoryImpl(get()) }
    single<AuditRepository> { AuditRepositoryImpl(get()) }
    single<StatesRepository> { TaskStatesRepositoryImpl(get()) }
    single<AuthenticationRepository>{ AuthenticationRepositoryImpl(get()) }
}