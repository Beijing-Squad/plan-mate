package data.remote.mongoDataSource.mongoConnection

import kotlinx.datetime.LocalDateTime
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class LocalDateTimeCodec : Codec<LocalDateTime> {

    override fun encode(writer: BsonWriter, value: LocalDateTime, encoderContext: EncoderContext) {
        writer.writeString(value.toString())
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): LocalDateTime {
        val dateString = reader.readString()
        return try {
            LocalDateTime.parse(dateString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse LocalDateTime from string: $dateString", e)
        }
    }

    override fun getEncoderClass(): Class<LocalDateTime> {
        return LocalDateTime::class.java
    }
}