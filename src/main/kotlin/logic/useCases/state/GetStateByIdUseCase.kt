package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class GetStateByIdUseCase(
    private val statesRepository: StatesRepository
) {
    fun getStateById(stateId: String): State? {
        return statesRepository.getStateById(stateId)
    }
}