package fake

import logic.entities.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createState(
    id: String = "DefaultStateId",
    name: String = "defaultStateName",
    projectId: String = "defaultProjectId"
): TaskState {
    return TaskState(
        id = Uuid.parse(id),
        name = name,
        projectId = projectId
    )
}