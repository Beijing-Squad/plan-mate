package data.csvDataSource

import data.repository.PasswordHashingDataSource
import java.security.MessageDigest

class MD5HashPasswordImpl: PasswordHashingDataSource {
    override fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}