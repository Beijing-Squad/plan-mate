package logic.useCases.state

import logic.entities.State
import logic.entities.UserRole
import logic.entities.exceptions.StateNotFoundException
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class DeleteStateUseCase(
    private val statesRepository: StatesRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun deleteState(state: State, role: UserRole): Boolean {

        if (role != UserRole.ADMIN) {
            throw StateUnauthorizedUserException("user should be Admin")
        }

        if (!isStateExist(state.id)) {
            throw StateNotFoundException("the state with this id not found")
        }

        return statesRepository.deleteState(state)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun isStateExist(stateId: String): Boolean = statesRepository.getAllStates().any { it.id == stateId }
}
