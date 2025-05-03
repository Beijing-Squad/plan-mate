package logic.repository

import logic.entities.User

interface AuthenticationRepository {

    fun registerUser(user: User): Boolean
    fun loginUser(username: String, password: String): User?
}