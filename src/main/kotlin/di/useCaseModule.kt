package di

import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import logic.useCases.task.UpdateTaskUseCase
import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(),get()) }
    single { RegisterUserAuthenticationUseCase(get(),get(),get()) }
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { UpdateTaskUseCase(get()) }

}
