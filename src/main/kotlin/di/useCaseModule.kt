package di

import logic.useCases.state.AddStateUseCase
import logic.useCases.state.DeleteStateUseCase
import logic.useCases.state.GetAllStatesUseCase
import logic.useCases.state.GetStateByIdUseCase
import logic.useCases.state.GetStatesByProjectIdUseCase
import logic.useCases.state.UpdateStateUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // Add use cases here
    single<AddStateUseCase> { AddStateUseCase(get()) }
    single<DeleteStateUseCase> { DeleteStateUseCase(get()) }
    single<GetStateByIdUseCase> { GetStateByIdUseCase(get()) }
    single<GetStatesByProjectIdUseCase> { GetStatesByProjectIdUseCase(get()) }
    single<GetAllStatesUseCase> { GetAllStatesUseCase(get()) }
    single< UpdateStateUseCase> { UpdateStateUseCase(get()) }

}
