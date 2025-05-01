package ui.screens

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import logic.entities.Project
import logic.entities.UserRole
import logic.useCases.project.*
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ProjectManagementScreen(
    private val addProjectUseCase: AddProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {
    override val id: String
        get() = "2"
    override val name: String
        get() = "Project Screen"

    override fun showOptionService() {
        consoleIO.show("\u001B[32mEnter option: \u001B[0m")
        consoleIO.showWithLine(
            """
            \u001B[36m╔════════════════════════════════╗
            ║      Project Management     ║
            ╚════════════════════════════════╝\u001B[0m
            
            \u001B[33m1\u001B[0m. 📋 List All Project
            \u001B[33m2\u001B[0m. 🔍 Find Project by ID
            \u001B[33m3\u001B[0m. ✏️ Update Project
            \u001B[33m4\u001B[0m. ➕ Add Project
            \u001B[33m5\u001B[0m. ➖ Delete Project
            \u001B[33m0\u001B[0m. 🔙 Exit to Main Menu
            
            \u001B[32mPlease select an option:\u001B[0m
             """.trimIndent()
        )
    }

    override fun handleFeatureChoice() {
        when (getInput()) {
            "1" -> listAllProjects()
            "2" -> findProjectById()
            "3" -> updateProject()
            "4" -> addProject()
            "5" -> deleteProject()
            "0" -> return
            else -> consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")

        }
    }

    private fun listAllProjects() {
        val result = getAllProjectsUseCase.getAllProjects(UserRole.ADMIN)
        result.fold(
            onSuccess = { projects ->
                if (projects.isEmpty()) {
                    consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
                } else {
                    projects.forEach { project ->
                        showProjectInfo(project)
                    }
                }
            },
            onFailure = { error ->
                consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m")
            }
        )
    }

    private fun findProjectById() {
        consoleIO.show("\u001B[32mEnter project ID: \u001B[0m")
        val id = getInput() ?: return
        val result = getProjectByIdUseCase.getProjectById(id, UserRole.ADMIN)
        result.fold(
            onSuccess = { project -> showProjectInfo(project) },
            onFailure = { error -> consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m") }
        )
    }

    private fun updateProject() {
        consoleIO.show("\u001B[32mEnter project ID to update: \u001B[0m")
        val id = getInput() ?: return
        val existing = getProjectByIdUseCase.getProjectById(id, UserRole.ADMIN)
        existing.fold(
            onSuccess = { project ->
                consoleIO.show("\u001B[32mEnter new name: \u001B[0m")
                val name = getInput() ?: return
                consoleIO.show("\u001B[32mEnter new description: \u001B[0m")
                val desc = getInput() ?: return
                val updated = project.copy(
                    name = name,
                    description = desc,
                    updatedAt = Clock.System.todayIn(TimeZone.currentSystemDefault())
                )
                val result = updateProjectUseCase.updateProject(updated, UserRole.ADMIN)
                result.fold(
                    onSuccess = {
                        consoleIO.showWithLine("\u001B[32m✅ Project updated successfully.\u001B[0m")
                    },
                    onFailure = { error ->
                        consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m")
                    }
                )
            },
            onFailure = { error -> consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m") }
        )
    }

    private fun addProject() {
        consoleIO.show("\u001B[32mEnter project name: \u001B[0m")
        val name = getInput() ?: return
        consoleIO.show("\u001B[32mEnter description: \u001B[0m")
        val desc = getInput() ?: return
        consoleIO.show("\u001B[32mEnter created by (user ID): \u001B[0m")
        val createdBy = getInput() ?: return
        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val newProject = Project(
            name = name,
            description = desc,
            createdBy = createdBy,
            createdAt = now,
            updatedAt = now
        )
        val result = addProjectUseCase.addProject(newProject, UserRole.ADMIN)
        result.fold(
            onSuccess = {
                consoleIO.showWithLine("\u001B[32m✅ Project added successfully.\u001B[0m")
            },
            onFailure = { error ->
                consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m")
            }
        )
    }

    private fun deleteProject() {
        consoleIO.show("\u001B[32mEnter project ID to delete: \u001B[0m")
        val id = getInput() ?: return
        val result = deleteProjectUseCase.deleteProject(id, UserRole.ADMIN)
        result.fold(
            onSuccess = {
                consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m")
            },
            onFailure = { error ->
                consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m")
            }
        )
    }

    private fun showProjectInfo(project: Project) {
        consoleIO.showWithLine(
            """
            \u001B[36m╭────────────────────────────╮
            │ ID: ${project.id}
            │ Name: ${project.name}
            │ Description: ${project.description}
            │ Created By: ${project.createdBy}
            │ Created At: ${project.createdAt}
            │ Updated At: ${project.updatedAt}
            ╰────────────────────────────╯\u001B[0m
            """.trimIndent()
        )
    }


}
