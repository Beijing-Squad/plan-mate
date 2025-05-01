package logic.useCases.state

import org.junit.jupiter.api.Assertions.*

class GetStatesByProjectIdUseCaseTest {

    private lateinit var getStatesByProjectIdUseCase: GetStatesByProjectIdUseCase
    private lateinit var stateRepository: StatesRepository


    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        getStatesByProjectIdUseCase = GetStatesByProjectIdUseCase(stateRepository)
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return states of project when are exist`() {
        // Given
        val project = createProject()
        val projectId = project.id.toString()
        val states = listOf(
            createState(
                projectId = projectId
            ),
            createState(
                projectId = project.id.toString()
            )
        )
        every { stateRepository.getStatesByProjectId(projectId) } returns states
        // When
        val result = getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
        // Then
        result.forEach { state ->
            assertThat(state.projectId).isEqualTo(projectId)
        }
    }
}