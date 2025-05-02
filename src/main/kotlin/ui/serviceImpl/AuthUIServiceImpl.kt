package ui.serviceImpl

import logic.useCases.ManageAuthenticationUseCase
import ui.service.AuthUIService
import ui.service.ConsoleIOService

class AuthUIServiceImpl(
    private val authUseCase: ManageAuthenticationUseCase,
    private val console: ConsoleIOService
) : AuthUIService {
    override fun login(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserRole(): String {
        TODO("Not yet implemented")
    }
}