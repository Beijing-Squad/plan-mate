package data.parser

import data.csvDataSource.csv.CsvParser
import logic.entities.User
import logic.entities.UserRole
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserCsvParser : CsvParser<User> {
    override fun header(): String = "id,userName,password,role"

    override fun serializer(item: User): String {
        return listOf(
            item.id.toString(),
            item.userName,
            item.password,
            item.role.name
        ).joinToString(",")
    }

    override fun deserializer(content: String): User {
        val parts = content.split(",")
        return User(
            id = Uuid.parse(parts[0]),
            userName = parts[1],
            password = parts[2],
            role = UserRole.valueOf(parts[3])
        )
    }
}
