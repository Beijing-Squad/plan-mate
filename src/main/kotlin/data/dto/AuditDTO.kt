package data.dto

import org.bson.codecs.pojo.annotations.BsonProperty


data class AuditDTO(
    @BsonProperty("id") val id: String,
    @BsonProperty("userRole") val userRole: String,
    val userName: String,
    val entityId: String,
    @BsonProperty("entityType") val entityType: String,
    @BsonProperty("action") val action: String,
    val actionDetails: String?,
    val timeStamp: String
)