package data.utils

import java.security.MessageDigest

fun hashPassword(password: String): String {
    return MessageDigest
        .getInstance("MD5")
        .digest(password.toByteArray())
        .joinToString("") { "%02x".format(it) }
}