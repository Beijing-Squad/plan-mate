package logic.useCases.state

import logic.entities.State
import logic.repository.StatesRepository

class GetAllStatesUseCase(
    private val statesRepository: StatesRepository
) {
    fun getAllStates(): List<State>{
        return statesRepository.getAllStates()
    }

}