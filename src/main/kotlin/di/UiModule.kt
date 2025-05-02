package di

import org.koin.dsl.module
import ui.screens.TaskManagementScreen

val uiModule = module {
    single { TaskManagementScreen(get(),get(),get(),get(),get(),get(),get()) }
}