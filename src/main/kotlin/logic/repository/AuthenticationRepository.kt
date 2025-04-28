package logic.repository

interface AuthenticationRepository{

    fun login(username: String, password: String): Boolean

}