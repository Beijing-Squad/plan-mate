package ui.screens

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import logic.entities.*
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManager
import logic.useCases.project.*
import ui.enums.ProjectBoardOption
import ui.main.BaseScreen
import ui.main.consoleIO.ConsoleIO
import ui.main.MenuRenderer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectManagementScreen(
    private val addProjectUseCase: AddProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val addAudit: AddAuditLogUseCase,
    private val userRole: UserRole = UserRole.ADMIN,
    private val consoleIO: ConsoleIO
) : BaseScreen(consoleIO) {

    private val sessionManager = SessionManager()
    override val id: String
        get() = "1"
    override val name: String
        get() = "Project Screen"

    override fun showOptionService() {
        MenuRenderer.renderMenu(
            """
        ╔════════════════════════════════════════╗
        ║        Project Management System       ║
        ╚════════════════════════════════════════╝
        """,
            ProjectBoardOption.entries,
            consoleIO
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
        val result = getAllProjectsUseCase.getAllProjects(userRole)
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
        val result = getProjectByIdUseCase.getProjectById(id, userRole)
        result.fold(
            onSuccess = { project -> showProjectInfo(project) },
            onFailure = { error -> consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m") }
        )
    }

    private fun updateProject() {
        consoleIO.show("\u001B[32mEnter project ID to update: \u001B[0m")
        val id = getInput() ?: return
        val existing = getProjectByIdUseCase.getProjectById(id, userRole)
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
                val result = updateProjectUseCase.updateProject(updated, userRole)
                result.fold(
                    onSuccess = {
                        consoleIO.showWithLine("\u001B[32m✅ Project updated successfully.\u001B[0m")
                        sessionManager.getCurrentUser()?.userName?.let { userName ->
                            addAudit.addAuditLog(
                                Audit(
                                    id = Uuid.random(),
                                    userRole = userRole,
                                    userName = userName,
                                    action = ActionType.UPDATE,
                                    entityType = EntityType.PROJECT,
                                    entityId = updated.id.toString(),
                                    oldState = updated.name,
                                    newState = updated.description,
                                    timeStamp = Clock.System.todayIn(TimeZone.currentSystemDefault())
                                )
                            )
                        }
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
        val result = addProjectUseCase.addProject(newProject, userRole)
        result.fold(
            onSuccess = {
                consoleIO.showWithLine("\u001B[32m✅ Project added successfully.\u001B[0m")
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = userRole,
                        userName = newProject.createdBy,
                        action = ActionType.CREATE,
                        entityType = EntityType.PROJECT,
                        entityId = newProject.id.toString(),
                        oldState = name,
                        newState = newProject.description,
                        timeStamp = now
                    )
                )
            },
            onFailure = { error ->
                consoleIO.showWithLine("\u001B[31m❌ ${error.message}\u001B[0m")
            }
        )
    }

    private fun deleteProject() {
        consoleIO.show("\u001B[32mEnter project ID to delete: \u001B[0m")
        val id = getInput() ?: return
        val result = deleteProjectUseCase.deleteProject(id, userRole)
        result.fold(
            onSuccess = {
                consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m")
                sessionManager.getCurrentUser()?.userName?.let { userName ->
                    addAudit.addAuditLog(
                        Audit(
                            id = Uuid.random(),
                            userRole = UserRole.ADMIN,
                            userName = userName,
                            action = ActionType.DELETE,
                            entityType = EntityType.PROJECT,
                            entityId = id,
                            oldState = "",
                            newState = "",
                            timeStamp = Clock.System.todayIn(TimeZone.currentSystemDefault())
                        )
                    )
                }
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
