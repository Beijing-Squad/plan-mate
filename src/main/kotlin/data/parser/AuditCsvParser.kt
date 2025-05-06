package data.parser

import data.csvDataSource.csv.CsvParser
import kotlinx.datetime.LocalDateTime
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AuditCsvParser : CsvParser<Audit> {
    override fun header(): String =
        "id,userRole,userName,entityId,entityType,action,oldState,newState,timeStamp"

    override fun serializer(item: Audit): String {
        return listOf(
            item.id.toString(),
            item.userRole.name,
            item.userName,
            item.entityId,
            item.entityType.name,
            item.action.name,
            item.oldState ?: "",
            item.newState ?: "",
            item.timeStamp.toString()
        ).joinToString(",")
    }

    override fun deserializer(content: String): Audit {
        val parts = content.split(",")
        return Audit(
            id = Uuid.parse(parts[0]),
            userRole = UserRole.valueOf(parts[1]),
            userName = parts[2],
            entityId = parts[3],
            entityType = EntityType.valueOf(parts[4]),
            action = ActionType.valueOf(parts[5]),
            oldState = parts[6].ifEmpty { null },
            newState = parts[7].ifEmpty { null },
            timeStamp = LocalDateTime.parse(parts[8])
        )
    }

    override fun getId(item: Audit): String {
        return item.id.toString()
    }
}
