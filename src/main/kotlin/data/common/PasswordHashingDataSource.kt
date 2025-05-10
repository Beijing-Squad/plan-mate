package data.common

interface PasswordHashingDataSource {
    fun hashPassword(password: String): String
}