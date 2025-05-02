package logic.factories

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object ProjectFactory {
    fun create(
        name: String,
        description: String,
        createdBy: String
    ): Project {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return Project(
            name = name,
            description = description,
            createdBy = createdBy,
            createdAt = today,
            updatedAt = today
        )
    }
}