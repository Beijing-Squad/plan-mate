package di

import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single { LoginUserAuthenticationUseCase(get()) }
    single { RegisterUserAuthenticationUseCase(get(),get()) }

}
