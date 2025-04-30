package logic.useCases.user

import logic.entities.User

class ValidateUserUseCase {

    fun validateUserName(userName: String): Boolean {
        return isUsernameInvalid(userName)
                ||isUserNameEmpty(userName)
    }

    fun validatePassword(password: String): Boolean {
        return isPasswordInvalid(password)
                || isPasswordEmpty(password)
    }

    private fun isUsernameInvalid(userName: String): Boolean {
        return !userName.matches(Regex(USER_PATTERN)) ||
                userName.length < MAX_USER_NAME_LENGTH
    }

    private fun isPasswordInvalid(password: String): Boolean {
        return password.length < MAX_PASSWORD_LENGTH
    }

    private fun isUserNameEmpty(userName: String): Boolean {
        return userName.isEmpty()
    }

    private fun isPasswordEmpty(password: String): Boolean {
        return password.isEmpty()
    }

    private companion object {
        const val MAX_USER_NAME_LENGTH = 3
        const val MAX_PASSWORD_LENGTH = 8
        const val USER_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$"
    }
}