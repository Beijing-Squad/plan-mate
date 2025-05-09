package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskDTO(
    @BsonProperty("id") val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val createdBy: String,
    val stateId: String,
    @BsonProperty("createdAt") val createdAt: String,
    @BsonProperty("updatedAt") val updatedAt: String
)