package data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskStateDTO(
    @BsonId val id: String,
    @BsonProperty("name") val name: String,
    @BsonProperty("projectId") val projectId: String
)