package di

import logic.useCases.project.*
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Add use cases here
    single { AddProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(),get()) }
    single { RegisterUserAuthenticationUseCase(get(),get(),get()) }
    
}
