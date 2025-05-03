package fake

import logic.entities.State

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