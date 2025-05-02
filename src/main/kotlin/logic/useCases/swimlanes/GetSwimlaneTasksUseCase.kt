package logic.useCases.swimlanes


import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.repository.ProjectsRepository
import logic.repository.StatesRepository
import logic.repository.TasksRepository


class GetSwimlaneTasksUseCase(
    private val taskRepo: TasksRepository,
    private val stateRepo: StatesRepository,
    private val projectRepo: ProjectsRepository
) {
    data class SwimlaneResult(
        val project: Project,
        val statesWithTasks: Map<State, List<Task>>
    )

    fun invoke(projectId: String): SwimlaneResult {
        val project = projectRepo.getProjectById(projectId)
        val states = stateRepo.getStatesByProjectId(projectId)
        val tasks = projectRepo.getTaskByProjectId(projectId)

        val grouped = states.associateWith { state ->
            tasks.filter { it.stateId == state.id }
        }

        return SwimlaneResult(project, grouped)
    }
}
