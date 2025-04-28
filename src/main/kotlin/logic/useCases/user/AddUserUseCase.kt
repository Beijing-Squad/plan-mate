package logic.useCases.user

import logic.entities.User
import logic.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi

class AddUserUseCase(
    private val userRepository: UserRepository
) {
}