package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateStateUseCase(
    private val statesRepository: StatesRepository,
    private val getStateByIdUseCase: GetStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateState(state: State): State {
        getStateByIdUseCase.getStateById(state.id)

        return statesRepository.updateState(state)
    }
}