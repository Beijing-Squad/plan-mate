package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class DeleteStateUseCase(
    private val statesRepository: StatesRepository
) {
    fun deleteState(state: State): Boolean {
        return false
    }
}
