package logic.entities

import kotlinx.datetime.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Task(
    @BsonId
    val id: Uuid = Uuid.random(),
    val projectId: String,
    val title: String,
    val description: String,
    val createdBy: String,
    val stateId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)