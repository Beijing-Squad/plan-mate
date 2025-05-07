package data.local.csvDataSource.parser

import data.local.csvDataSource.csv.CsvParser
import logic.entities.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskStateCsvParser : CsvParser<TaskState> {
    override fun header(): String = "id,name,projectId"

    @OptIn(ExperimentalUuidApi::class)
    override fun serializer(item: TaskState): String {
        return listOf(
            item.id,
            item.name,
            item.projectId
        ).joinToString(",")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deserializer(content: String): TaskState {
        val parts = content.split(",")
        return TaskState(
            id = Uuid.Companion.parse(parts[0]),
            name = parts[1],
            projectId = Uuid.parse(parts[2])
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getId(item: TaskState): String {
        return item.id.toString()
    }
}