package logic.useCases.state

import logic.entity.TaskState
import logic.repository.StatesRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskStatesByProjectIdUseCase(
    private val statesRepository: StatesRepository
) {
    suspend fun getTaskStatesByProjectId(projectId: Uuid): List<TaskState> {

        return statesRepository.getTaskStatesByProjectId(projectId)
    }
}