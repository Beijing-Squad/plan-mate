package di

import data.csvDataSource.csv.CsvReader
import data.csvDataSource.csv.CsvWriter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val csvModule = module {
    // Readers
    single(named("projectReader")) { CsvReader(get(named("projectFile"))) }
    single(named("userReader")) { CsvReader(get(named("userFile"))) }
    single(named("taskReader")) { CsvReader(get(named("taskFile"))) }
    single(named("stateReader")) { CsvReader(get(named("stateFile"))) }
    single(named("auditReader")) { CsvReader(get(named("auditFile"))) }
    single(named("authenticationReader")) {CsvReader(get(named("userFile")))}

    // Writers
    single(named("projectWriter")) { CsvWriter(get(named("projectFile"))) }
    single(named("userWriter")) { CsvWriter(get(named("userFile"))) }
    single(named("taskWriter")) { CsvWriter(get(named("taskFile"))) }
    single(named("stateWriter")) { CsvWriter(get(named("stateFile"))) }
    single(named("auditWriter")) { CsvWriter(get(named("auditFile"))) }
    single(named("authenticationWriter")) {CsvWriter(get(named("userFile")))}
}
