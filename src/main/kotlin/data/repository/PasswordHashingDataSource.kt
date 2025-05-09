package data.repository

interface PasswordHashingDataSource {
    fun hashPassword(password: String): String
}