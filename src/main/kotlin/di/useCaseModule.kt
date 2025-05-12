package di

import GetAuditLogsByProjectIdUseCase
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.audit.GetAllAuditLogsUseCase
import logic.useCases.audit.GetAuditLogsByTaskIdUseCase
import logic.useCases.authentication.LoginUserAuthenticationUseCase
import logic.useCases.authentication.RegisterUserAuthenticationUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.project.*
import logic.useCases.state.*
import logic.useCases.task.*
import logic.useCases.user.*
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUserAuthenticationUseCase(get(), get()) }
    single { RegisterUserAuthenticationUseCase(get()) }
    single { SessionManagerUseCase() }

    single { AddProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { UpdateProjectUseCase(get()) }

    single { AddTaskStateUseCase(get()) }
    single { DeleteTaskStateUseCase(get()) }
    single { GetTaskStateByIdUseCase(get()) }
    single { GetTaskStatesByProjectIdUseCase(get()) }
    single { GetAllTaskStatesUseCase(get()) }
    single { UpdateTaskStateUseCase(get()) }

    single { GetAllUsersUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { UpdateUserUseCase(get()) }

    single { AddAuditLogUseCase(get(),get()) }
    single { GetAllAuditLogsUseCase(get()) }
    single { GetAuditLogsByProjectIdUseCase(get()) }
    single { GetAuditLogsByTaskIdUseCase(get()) }

    single { AddTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { UpdateTaskUseCase(get()) }
}

