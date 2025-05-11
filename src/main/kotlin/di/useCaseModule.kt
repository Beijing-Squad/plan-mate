package di

import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.project.*
import logic.useCases.state.*
import logic.useCases.task.*
import logic.useCases.user.GetAllUsersUseCase
import logic.useCases.user.GetUserByIdUseCase
import logic.useCases.user.UpdateUserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Add use cases here
    single { AddProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { LoginUserAuthenticationUseCase(get(), get()) }
    single { RegisterUserAuthenticationUseCase(get()) }
    single { SessionManagerUseCase() }
    // state use cases
    single { AddTaskStateUseCase(get()) }
    single { DeleteTaskStateUseCase(get()) }
    single { GetTaskStateByIdUseCase(get()) }
    single { GetTaskStatesByProjectIdUseCase(get()) }
    single { GetAllTaskStatesUseCase(get()) }
    single { UpdateTaskStateUseCase(get()) }

    single { GetAllUsersUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { UpdateUserUseCase(get()) }

    single { AddAuditLogUseCase(get()) }
    single { GetAllAuditLogsUseCase(get()) }
    single { GetAuditLogsByProjectIdUseCase(get()) }
    single { GetAuditLogsByTaskIdUseCase(get()) }

    // task use cases
    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { UpdateTaskUseCase(get()) }
}

