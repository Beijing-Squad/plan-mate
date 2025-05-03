import di.appModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import ui.screens.AuthenticationScreen

fun main() {

    startKoin {
        modules(appModule)
    }

    val authenticationScreen: AuthenticationScreen = getKoin().get()
    authenticationScreen.start()
}