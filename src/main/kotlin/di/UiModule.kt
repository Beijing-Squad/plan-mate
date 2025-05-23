package di

import logic.entities.UserRole
import org.koin.dsl.module
import ui.console.SwimlanesRenderer
import ui.main.consoleIO.ConsoleIO
import ui.main.consoleIO.SystemConsoleIOImpl
import ui.screens.*

val uiModule = module {

    single<ConsoleIO> { SystemConsoleIOImpl() }
    single { SwimlanesRenderer(get()) }

    single {
        AuthenticationScreen(
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        ProjectManagementScreen(
            get(), get(), get(), get(), get(),
            get(),
            UserRole.ADMIN,
            get()
        )
    }

    single {
        AdminScreen(
            get(), get(), get(),
            get(), get(), get(),
            get(), get()
        )
    }

    single {
        MateScreen(
            get(), get(), get(),
            get(), get(), get()
        )
    }

    single {
        TaskStateScreen(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get()
        )
    }

    single {
        TaskManagementScreen(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get(),
            get()
        )
    }

    single {
        AuditScreen(
            get(), get(), get(), get()
        )
    }

    single {
        UserScreen(
            get(), get(), get(), get(), get()
        )
    }
}