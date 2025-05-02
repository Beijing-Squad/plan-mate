package di

import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManager
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import logic.useCases.state.*
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(), get(), get()) }
    single { RegisterUserAuthenticationUseCase(get(), get(), get()) }
    single { SessionManager() }
    single { AddStateUseCase(get()) }
    single { DeleteStateUseCase(get()) }
    single { GetStateByIdUseCase(get()) }
    single { GetStatesByProjectIdUseCase(get()) }
    single { GetAllStatesUseCase(get()) }
    single { UpdateStateUseCase(get()) }

    single { GetAllUsersUseCase(get()) }
    single { GetUserByUserIdUseCase(get()) }
    single { UpdateUserUseCase(get(), get(), get()) }
}