package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class DeleteTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getTaskStateByIdUseCase: GetTaskStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun deleteState(state: State): Boolean {
        val existedState = getTaskStateByIdUseCase.getStateById(state.id)
        return statesRepository.deleteState(existedState)
    }
}