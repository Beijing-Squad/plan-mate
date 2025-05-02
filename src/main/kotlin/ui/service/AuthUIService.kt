package ui.service

interface AuthUIService {
    fun login(): Boolean
    fun getUserRole(): String
}