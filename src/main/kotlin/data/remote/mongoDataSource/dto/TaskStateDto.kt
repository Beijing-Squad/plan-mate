package data.remote.mongoDataSource.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskStateDto(
    @BsonId val id: String,
    @BsonProperty("name") val name: String,
    @BsonProperty("projectId") val projectId: String
)