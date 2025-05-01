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

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should throw exception when not found states with the project id`() {
        // Given
        val errorMessage = "Not found states with this project id"
        val projectId = createProject().id.toString()
        val states = listOf(
            createState(),
            createState()
        )

        every { stateRepository.getAllStates() } returns  states
        every { stateRepository.getStatesByProjectId(projectId) } throws StateNotFoundException(errorMessage)

        // When & Then
        assertThrows<StateNotFoundException> {
            getStatesByProjectIdUseCase.getStatesByProjectId(projectId)
            }
        }
}