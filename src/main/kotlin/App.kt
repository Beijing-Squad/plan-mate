import di.repositoryModule
import di.serviceModule
import di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import ui.MainMenuController

fun main() {
    startKoin {
        modules(repositoryModule, useCaseModule, serviceModule)
    }

    val mainMenuController = getKoin().get<MainMenuController>()
    mainMenuController.showMainMenu()
}
