package di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val fileModule = module {
    single(named("projectFile")) { File("resource/project.csv") }
    single(named("userFile")) { File("resource/user.csv") }
    single(named("taskFile")) { File("resource/task.csv") }
    single(named("stateFile")) { File("resource/state.csv") }
    single(named("auditFile")) { File("resource/audit.csv") }
}
