package di

import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import logic.useCases.task.UpdateTaskUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here

    single { GetAllTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { UpdateTaskUseCase(get()) }

}
