package data.csvDataSource

import data.repository.ValidationUserDataSource
import logic.entities.exceptions.InvalidPasswordException
import logic.entities.exceptions.InvalidUserNameException

class ValidationUserDataSourceImpl: ValidationUserDataSource {
    override fun validateUsername(username: String) {
        if (username.isBlank()) {
            throw InvalidUserNameException("Username cannot be blank")
        }
    }

    override fun validatePassword(password: String) {
        if (password.isBlank()) {
            throw InvalidPasswordException("Password cannot be blank")
        }
    }
}