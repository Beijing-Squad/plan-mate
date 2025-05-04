package data.parser

import data.csvDataSource.csv.CsvParser
import kotlinx.datetime.LocalDateTime
import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
class TaskCsvParser : CsvParser<Task> {
    override fun header(): String =
        "id,projectId,title,description,createdBy,stateId,createdAt,updatedAt"

    override fun serializer(item: Task): String {
        return listOf(
            item.id.toString(),
            item.projectId,
            item.title,
            item.description,
            item.createdBy,
            item.stateId,
            item.createdAt.toString(),
            item.updatedAt.toString()
        ).joinToString(",")
    }

    override fun deserializer(content: String): Task {
        val parts = content.split(",")
        return Task(
            id = Uuid.parse(parts[0]),
            projectId = parts[1],
            title = parts[2],
            description = parts[3],
            createdBy = parts[4],
            stateId = parts[5],
            createdAt = LocalDateTime.parse(parts[6]),
            updatedAt = LocalDateTime.parse(parts[7])
        )
    }

    override fun getId(item: Task): String {
        return item.id.toString()
    }
}
