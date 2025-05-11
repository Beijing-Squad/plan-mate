package logic.useCases.state

import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteTaskStateUseCase(
    private val statesRepository: StatesRepository,
    private val getTaskStateByIdUseCase: GetTaskStateByIdUseCase
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteState(stateId: Uuid): Boolean {
        val existedState = getTaskStateByIdUseCase.getStateById(stateId.toString())
        return statesRepository.deleteState(existedState)
    }
}