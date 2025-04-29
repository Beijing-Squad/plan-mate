package logic.useCases.user.cryptography

import java.security.MessageDigest

class MD5PasswordEncryption: PasswordEncryption {
    @OptIn(ExperimentalStdlibApi::class)
    override fun encryptionPassword(password: String): String {
        return MessageDigest
            .getInstance(ALGORITHM)
            .digest(password.toByteArray())
            .toHexString()
    }

    private companion object{
        const val ALGORITHM = "MD5"
    }
}