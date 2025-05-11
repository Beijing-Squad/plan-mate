package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty

data class ProjectDto (
    @BsonProperty("id")
    val id: String,
    @BsonProperty("name")
    val name: String,
    @BsonProperty("name")
    val description: String,
    @BsonProperty("createdBy")
    val createdBy: String,
    @BsonProperty("createdAt")
    val createdAt: String,
    @BsonProperty("updatedAt")
    val updatedAt: String
)