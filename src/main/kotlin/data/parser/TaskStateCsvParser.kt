package data.parser

import data.csvDataSource.csv.CsvParser
import logic.entities.State
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskStateCsvParser : CsvParser<State> {
    override fun header(): String = "id,name,projectId"

    @OptIn(ExperimentalUuidApi::class)
    override fun serializer(item: State): String {
        return listOf(
            item.id,
            item.name,
            item.projectId
        ).joinToString(",")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun deserializer(content: String): State {
        val parts = content.split(",")
        return State(
            id = Uuid.parse(parts[0]),
            name = parts[1],
            projectId = parts[2]
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getId(item: State): String {
        return item.id.toString()
    }
}
