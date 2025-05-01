package di

import logic.useCases.task.AddTaskUseCase
import logic.useCases.task.DeleteTaskUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
}
