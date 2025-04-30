package data.csvDataSource

import data.csvDataSource.csv.CsvDataSourceImpl
import io.mockk.mockk
import logic.entities.Audit
import org.junit.jupiter.api.BeforeEach

class AuditCsvDataSourceImplTest {

    private lateinit var csvDataSource: CsvDataSourceImpl<Audit>
    private lateinit var auditDataSource: AuditCsvDataSourceImpl

    @BeforeEach
    fun setUp() {
        csvDataSource = mockk(relaxed = true)
        auditDataSource = AuditCsvDataSourceImpl(csvDataSource)
    }

}