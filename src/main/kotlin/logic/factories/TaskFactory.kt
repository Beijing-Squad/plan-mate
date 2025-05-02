package logic.factories

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object TaskFactory {
    fun create(
        projectId: String,
        title: String,
        description: String,
        createdBy: String,
        stateId: String
    ): Task {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return Task(
            projectId = projectId,
            title = title,
            description = description,
            createdBy = createdBy,
            stateId = stateId,
            createdAt = today,
            updatedAt = today
        )
    }
}