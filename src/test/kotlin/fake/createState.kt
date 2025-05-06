package fake

import logic.entities.TaskState

fun createState(
    id: String = "DefaultStateId",
    name: String = "defaultStateName",
    projectId: String = "defaultProjectId"
): TaskState {
    return TaskState(
        id = id,
        name = name,
        projectId = projectId
    )
}