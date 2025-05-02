package di

import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(),get()) }
    single { RegisterUserAuthenticationUseCase(get(),get(),get()) }

}
