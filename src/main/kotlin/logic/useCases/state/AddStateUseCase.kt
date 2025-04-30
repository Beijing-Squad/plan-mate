package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class AddStateUseCase(
    private val statesRepository: StatesRepository
) {
    fun addState(state: State): Boolean {
        return statesRepository.addState(state)
    }
}