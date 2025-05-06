package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getTaskStateByIdUseCase: GetTaskStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateState(state: State): State {
        getTaskStateByIdUseCase.getStateById(state.id.toString())

        return statesRepository.updateState(state)
    }
}