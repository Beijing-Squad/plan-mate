import di.appModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin
import ui.screens.AuthenticationScreen

fun main() {

    System.setProperty("MONGO_CONNECTION_STRING", "mongodb+srv://radwamohamed5033:itGzVSNEk5JDnN95@cluster0.pcitphl.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    startKoin {
        modules(appModule)
    }

    val authenticationScreen: AuthenticationScreen = getKoin().get()
    authenticationScreen.start()
}