package di

import ui.MainMenuController
import org.koin.dsl.module
import ui.service.ConsoleIOService
import ui.serviceImpl.*
import ui.serviceImpl.AuditUIServiceImpl
import ui.serviceImpl.ProjectUIServiceImpl
import ui.serviceImpl.StateUIServiceImpl
import ui.serviceImpl.TaskUIServiceImpl
import ui.serviceImpl.AuthUIServiceImpl
import ui.serviceImpl.SwimlaneUIServiceImpl

val serviceModule = module {
    single<ConsoleIOService> { ConsoleIOServiceImpl() }
    single { AuthUIServiceImpl(get(), get()) }
    single { ProjectUIServiceImpl(get(), get(), get(), get(), get(), get()) }
    single { TaskUIServiceImpl(get(), get(), get(), get(), get()) }
    single { StateUIServiceImpl(get(), get(), get(), get(), get(), get(), get()) }
    single { AuditUIServiceImpl(get(), get(), get(), get(), get()) }
    single { SwimlaneUIServiceImpl(get(), get()) }
    single { MainMenuController(get(), get(), get(),get(), get(), get(),get()) }
}
