package ui.serviceImpl

import logic.entities.State
import logic.useCases.state.*
import ui.service.ConsoleIOService
import ui.service.StateUIService

class StateUIServiceImpl(
    private val addStateUseCase: AddStateUseCase,
    private val deleteStateUseCase: DeleteStateUseCase,
    private val getStateByIdUseCase: GetStateByIdUseCase,
    private val getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    private val getAllStatesUseCase: GetAllStatesUseCase,
    private val console: ConsoleIOService
): StateUIService {
    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: String): State {
        TODO("Not yet implemented")
    }

    override fun addState(state: State) {
        TODO("Not yet implemented")
    }

    override fun updateState(state: State) {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: State) {
        TODO("Not yet implemented")
    }
}
