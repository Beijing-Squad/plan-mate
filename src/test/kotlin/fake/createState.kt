package fake

import logic.entities.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createState(
    id: Uuid = Uuid.random(),
    name: String = "defaultStateName",
    projectId: String = Uuid.random().toString()
): TaskState {
    return TaskState(
        id = id,
        name = name,
        projectId = Uuid.parse(projectId)
    )
}