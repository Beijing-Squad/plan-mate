package data.common

import java.security.MessageDigest

class MD5HashPasswordImpl: PasswordHashingDataSource {
    override fun hashPassword(password: String): String {
        return MessageDigest
            .getInstance(ALGORITHM)
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    private companion object{
        const val ALGORITHM = "MD5"
    }
}