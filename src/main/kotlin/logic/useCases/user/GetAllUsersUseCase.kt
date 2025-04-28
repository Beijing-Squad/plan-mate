package logic.useCases.user

import logic.repository.UserRepository

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
}