package logic.useCases.state

import logic.entities.State
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository

class GetAllStatesUseCase(
    private val statesRepository: StatesRepository
) {
    fun getAllStates(): List<State>{
        require(statesRepository.getAllStates().isNotEmpty()) {
            throw StateNotFoundException("No States Found")
        }
        return statesRepository.getAllStates()
    }
}