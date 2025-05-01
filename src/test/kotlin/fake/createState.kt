package fake

import logic.entities.State
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createState(
    id: String = "DefaultStateId",
    name: String = "defaultStateName",
    projectId: String = "defaultProjectId"
): State {
    return State(
        id = id,
        name = name,
        projectId = projectId
    )
}