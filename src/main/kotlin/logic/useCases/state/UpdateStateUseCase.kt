package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class UpdateStateUseCase(
    private val statesRepository: StatesRepository
) {
    fun updateState(state: State): Boolean{
        return statesRepository.updateState(state)
    }
}