import di.appModule
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManager
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import ui.screens.AuthenticationScreen

fun main() {
    println("Hello World!")
    startKoin{modules(appModule)}
    val loginUserAuthenticationUseCase : LoginUserAuthenticationUseCase = getKoin().get()
    val registerUserAuthenticationUseCase : RegisterUserAuthenticationUseCase = getKoin().get()
    val sessionManager : SessionManager = getKoin().get()
    val authenticationScreen =
        AuthenticationScreen(registerUserAuthenticationUseCase,loginUserAuthenticationUseCase, sessionManager)
    authenticationScreen.start()
}