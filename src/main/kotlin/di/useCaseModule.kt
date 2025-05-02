package di

import logic.useCases.task.GetAllTasksUseCase
import logic.useCases.task.GetTaskByIdUseCase
import logic.useCases.task.UpdateTaskUseCase
import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { UpdateTaskUseCase(get()) }

}
