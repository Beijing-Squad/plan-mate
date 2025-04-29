package logic.useCases.user.cryptography

interface PasswordEncryption {
    fun encryptionPassword(password: String): String
}