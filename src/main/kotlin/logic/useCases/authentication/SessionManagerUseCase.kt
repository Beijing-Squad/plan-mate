package logic.useCases.authentication

import logic.entity.User

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