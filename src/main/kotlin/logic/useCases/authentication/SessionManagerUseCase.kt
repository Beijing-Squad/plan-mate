package logic.useCases.authentication

import logic.entities.User

class SessionManagerUseCase {
    private var currentUser: User? = null

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user: User) {
        currentUser = user
    }

    fun clearCurrentUser() {
        currentUser = null
    }
}