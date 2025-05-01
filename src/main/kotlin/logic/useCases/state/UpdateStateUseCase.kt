package logic.useCases.state

import logic.entities.State
import logic.entities.UserRole
import logic.entities.exceptions.StateNotFoundException
import logic.entities.exceptions.StateUnauthorizedUserException
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi

class UpdateStateUseCase(
    private val statesRepository: StatesRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun updateState(state: State, role: UserRole): State {
        if (role != UserRole.ADMIN) {
            throw StateUnauthorizedUserException("user should be Admin")
        }

        val currentState = statesRepository.getStateById(state.id.toString())
            ?: throw StateNotFoundException("State with this id not found")

        val newState = currentState.copy(
            name = state.name,
            projectId = state.projectId
        )

        return statesRepository.updateState(newState)
    }
}