package data.parser

import data.csvDataSource.csv.CsvParser
import logic.entities.State

class TaskStateCsvParser : CsvParser<State> {
    override fun header(): String = "id,name,projectId"

    override fun serializer(item: State): String {
        return listOf(
            item.id,
            item.name,
            item.projectId
        ).joinToString(",")
    }

    override fun deserializer(content: String): State {
        val parts = content.split(",")
        return State(
            id = parts[0],
            name = parts[1],
            projectId = parts[2]
        )
    }

    override fun getId(item: State): String {
        return item.id
    }
}
