package di

import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import logic.useCases.user.ValidateUserUseCase
import logic.useCases.user.cryptography.MD5PasswordEncryption
import logic.useCases.user.cryptography.PasswordEncryption
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here




    single { GetAllUsersUseCase(get()) }
    single { GetUserByUserIdUseCase(get()) }
    single<PasswordEncryption> { MD5PasswordEncryption() }
    single { ValidateUserUseCase() }
    single { UpdateUserUseCase(get(),get(),get()) }

}
