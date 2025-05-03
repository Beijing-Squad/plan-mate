import di.appModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import ui.main.PlanMateConsoleUi
import ui.screens.AuthenticationScreen

fun main() {
    println("Hello World!")

    startKoin {
        modules(appModule)
    }

    val authenticationScreen: AuthenticationScreen = getKoin().get()
    authenticationScreen.start()
}