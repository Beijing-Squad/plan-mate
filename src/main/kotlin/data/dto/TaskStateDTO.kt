package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskStateDTO(
    @BsonProperty("id") val id: String,
    @BsonProperty("name") val name: String,
    @BsonProperty("projectId") val projectId: String
)