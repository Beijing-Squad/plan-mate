package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class DeleteStateUseCase(
    private val statesRepository: StatesRepository,
    private val getStateByIdUseCase: GetStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun deleteState(state: State): Boolean {
        val existedState = getStateByIdUseCase.getStateById(state.id)
        return statesRepository.deleteState(existedState)
    }
}