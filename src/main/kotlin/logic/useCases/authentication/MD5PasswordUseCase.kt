package logic.useCases.authentication

import java.security.MessageDigest

class MD5PasswordUseCase {
    fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}