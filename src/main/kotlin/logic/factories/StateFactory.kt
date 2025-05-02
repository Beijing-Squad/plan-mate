package logic.factories

import logic.entities.State

object StateFactory {
    fun create(
        id: String,
        name: String,
        projectId: String
    ): State {
        return State(
            id = id,
            name = name,
            projectId = projectId
        )
    }
}