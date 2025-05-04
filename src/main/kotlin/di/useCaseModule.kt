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
import logic.useCases.user.GetUserByIdUseCase
import logic.useCases.user.UpdateUserUseCase
import logic.useCases.state.*
import logic.useCases.task.*
import logic.useCases.user.ValidationUserUseCase
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
    // state use cases
    single { AddStateUseCase(get(),get()) }
    single { DeleteStateUseCase(get()) }
    single { GetStateByIdUseCase(get()) }
    single { GetStatesByProjectIdUseCase(get()) }
    single { GetAllStatesUseCase(get()) }
    single { UpdateStateUseCase(get()) }

    single { GetAllUsersUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { ValidationUserUseCase() }
    single { UpdateUserUseCase(get(), get()) }

    single { AddAuditLogUseCase(get())}
    single { GetAllAuditLogsUseCase(get())}
    single { GetAuditLogsByProjectIdUseCase(get())}
    single { GetAuditLogsByTaskIdUseCase(get())}

    // task use cases
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { UpdateTaskUseCase(get()) }
}

