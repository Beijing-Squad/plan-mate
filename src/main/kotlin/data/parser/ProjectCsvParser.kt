package data.parser

import data.csvDataSource.csv.CsvParser
import kotlinx.datetime.LocalDate
import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectCsvParser : CsvParser<Project> {
    override fun header(): String = "id,name,description,createdBy,createdAt,updatedAt"

    override fun serializer(item: Project): String {
        return listOf(
            item.id.toString(),
            item.name,
            item.description,
            item.createdBy,
            item.createdAt.toString(),
            item.updatedAt.toString()
        ).joinToString(",")
    }

    override fun deserializer(content: String): Project {
        val parts = content.split(",")
        return Project(
            id = Uuid.parse(parts[0]),
            name = parts[1],
            description = parts[2],
            createdBy = parts[3],
            createdAt = LocalDate.parse(parts[4]),
            updatedAt = LocalDate.parse(parts[5])
        )
    }
}
