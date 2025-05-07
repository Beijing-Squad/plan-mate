package data.repository

interface ValidationUserDataSource {
    fun validateUsername(userName: String)
    fun validatePassword(password: String)
}