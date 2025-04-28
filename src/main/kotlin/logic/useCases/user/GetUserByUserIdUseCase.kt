package logic.useCases.user

import logic.repository.UserRepository

class GetUserByUserIdUseCase(
    private val userRepository: UserRepository
) {
}
