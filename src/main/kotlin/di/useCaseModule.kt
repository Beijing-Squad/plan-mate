package di

import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManager
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import logic.useCases.state.AddStateUseCase
import logic.useCases.state.DeleteStateUseCase
import logic.useCases.state.GetAllStatesUseCase
import logic.useCases.state.GetStateByIdUseCase
import logic.useCases.state.GetStatesByProjectIdUseCase
import logic.useCases.state.UpdateStateUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(), get(), get()) }
    single { RegisterUserAuthenticationUseCase(get(), get(), get()) }
    single { SessionManager() }
    single<AddStateUseCase> { AddStateUseCase(get()) }
    single<DeleteStateUseCase> { DeleteStateUseCase(get()) }
    single<GetStateByIdUseCase> { GetStateByIdUseCase(get()) }
    single<GetStatesByProjectIdUseCase> { GetStatesByProjectIdUseCase(get()) }
    single<GetAllStatesUseCase> { GetAllStatesUseCase(get()) }
    single< UpdateStateUseCase> { UpdateStateUseCase(get()) }

    single { GetAllUsersUseCase(get()) }
    single { GetUserByUserIdUseCase(get()) }
    single { UpdateUserUseCase(get(), get(), get()) }
}