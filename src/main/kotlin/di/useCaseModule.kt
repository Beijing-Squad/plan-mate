package di

import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import logic.useCases.project.*
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.MD5PasswordUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManager
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByUserIdUseCase
import logic.useCases.user.UpdateUserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Add use cases here
    single { AddProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { MD5PasswordUseCase() }
    single { LoginUserAuthenticationUseCase(get(), get(), get()) }
    single { RegisterUserAuthenticationUseCase(get(), get(), get()) }
    single { SessionManager() }
    single { GetAllUsersUseCase(get()) }
    single { GetUserByUserIdUseCase(get()) }
    single { UpdateUserUseCase(get(), get(), get()) }
    single { AddAuditLogUseCase(get())}
    single { GetAllAuditLogsUseCase(get())}
    single { GetAuditLogsByProjectIdUseCase(get())}
    single { GetAuditLogsByTaskIdUseCase(get())}
}

