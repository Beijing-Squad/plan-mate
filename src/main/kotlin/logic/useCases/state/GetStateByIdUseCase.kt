package logic.useCases.state

import logic.entities.State
import logic.entities.exceptions.StateNotFoundException
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class GetStateByIdUseCase(
    private val statesRepository: StatesRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun getStateById(stateId: String): State? {
        return statesRepository.getStateById(stateId)
            ?: throw StateNotFoundException("state not found")
    }
}