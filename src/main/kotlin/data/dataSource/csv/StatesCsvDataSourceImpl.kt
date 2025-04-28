package data.dataSource.csv

import data.dataSource.csv.utils.CsvPlanMateParser
import data.dataSource.csv.utils.CsvPlanMateReader
import data.repository.dataSourceAbstraction.StatesDataSource
import logic.entities.State
import java.io.File

class StatesCsvDataSourceImpl(
    private val csvPlanMateParser: CsvPlanMateParser,
    private val csvPlanMateReader: CsvPlanMateReader
): StatesDataSource{

    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStatesByProjectId(projectId: String): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: String): State {
        TODO("Not yet implemented")
    }

    override fun addState(state: State) {
        TODO("Not yet implemented")
    }

    override fun updateState(state: State) {
        TODO("Not yet implemented")
    }

    override fun deleteState(state: State) {
        TODO("Not yet implemented")
    }

}