package ui.screens

import format
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.Audit
import logic.entities.Project
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.project.*
import ui.enums.ProjectBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ProjectManagementScreen(
    private val addProjectUseCase: AddProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val addAudit: AddAuditLogUseCase,
    private val consoleIO: ConsoleIO,
    private val sessionManager: SessionManagerUseCase
) : BaseScreen(consoleIO) {

    override val id: String
        get() = "1"
    override val name: String
        get() = "Project Screen"
    private val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

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
        while (true) {
            when (getInput()) {
                "1" -> listAllProjects()
                "2" -> findProjectById()
                "3" -> updateProject()
                "4" -> addProject()
                "5" -> deleteProject()
                "0" -> {
                    consoleIO.showWithLine("\u001B[34m🔙 Returning to Main Menu...\u001B[0m")
                    break
                }

                else -> consoleIO.showWithLine("\u001B[31m❌ Invalid Option\u001B[0m")

            }
            showOptionService()
        }
    }

    private fun listAllProjects() {
        try {
            showAnimation("list all project...") {
                val projects = getAllProjectsUseCase.getAllProjects()
                if (projects.isEmpty()) {
                    consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
                } else {
                    projects.forEach { project ->
                        consoleIO.showWithLine("")
                        showProjectInfo(project)
                    }
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun findProjectById() {
        try {
            consoleIO.show("\u001B[32mEnter project ID: \u001B[0m")
            val id = getInput() ?: return
            showAnimation("find project by id...") {
                consoleIO.showWithLine("")
                val project = getProjectByIdUseCase.getProjectById(id)
                showProjectInfo(project)
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun updateProject() {
        try {
            consoleIO.show("\u001B[32mEnter project ID to update: \u001B[0m")
            val id = getInput() ?: return
            showAnimation("update project...") {
                consoleIO.showWithLine("")
                val project = getProjectByIdUseCase.getProjectById(id)
                consoleIO.show("\u001B[32mEnter new name: \u001B[0m")
                val name = getInput() ?: return@showAnimation
                consoleIO.show("\u001B[32mEnter new description: \u001B[0m")
                val desc = getInput() ?: return@showAnimation

                val updated = project.copy(
                    name = name,
                    description = desc,
                    updatedAt = now
                )

                updateProjectUseCase.updateProject(updated)
                consoleIO.showWithLine("\u001B[32m✅ Project updated successfully.\u001B[0m")
                sessionManager.getCurrentUser()?.let { user ->
                    val actionDetails =
                        "Admin ${user.userName} updated project ${updated.id} with name '$name' at ${now.format()}"
                    addAudit.addAuditLog(
                        Audit(
                            id = user.id,
                            userRole = user.role,
                            userName = user.userName,
                            action = Audit.ActionType.UPDATE,
                            entityType = Audit.EntityType.PROJECT,
                            entityId = updated.id.toString(),
                            actionDetails = actionDetails,
                            timeStamp = now
                        )
                    )
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun addProject() {
        try {
            consoleIO.show("\u001B[32mEnter project name: \u001B[0m")
            val name = getInput() ?: return
            consoleIO.show("\u001B[32mEnter description: \u001B[0m")
            val desc = getInput() ?: return
            sessionManager.getCurrentUser()?.let { user ->
                val newProject = Project(
                    name = name,
                    description = desc,
                    createdBy = user.id.toString(),
                    createdAt = now,
                    updatedAt = now
                )
                showAnimation("add project...") {
                    consoleIO.showWithLine("")
                    addProjectUseCase.addProject(newProject)
                    consoleIO.showWithLine("\u001B[32m✅ Project added successfully.\u001B[0m")
                    val actionDetails =
                        "Admin ${user.userName} created project ${newProject.id} with name '$name' at ${now.format()}"
                    addAudit.addAuditLog(
                        Audit(
                            id = user.id,
                            userRole = user.role,
                            userName = user.userName,
                            action = Audit.ActionType.CREATE,
                            entityType = Audit.EntityType.PROJECT,
                            entityId = newProject.id.toString(),
                            actionDetails = actionDetails,
                            timeStamp = now
                        )
                    )
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun deleteProject() {
        try {
            consoleIO.show("\u001B[32mEnter project ID to delete: \u001B[0m")
            val id = getInput() ?: return
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            showAnimation("delete project...") {
                consoleIO.showWithLine("")
                deleteProjectUseCase.deleteProject(id)
                consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m")
                sessionManager.getCurrentUser()?.let { user ->
                    val actionDetails = "Admin ${user.userName} deleted project with name '$name' at ${now.format()}"
                    addAudit.addAuditLog(
                        Audit(
                            id = user.id,
                            userRole = user.role,
                            userName = user.userName,
                            action = Audit.ActionType.DELETE,
                            entityType = Audit.EntityType.PROJECT,
                            entityId = id,
                            actionDetails = actionDetails,
                            timeStamp = now
                        )
                    )
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun showProjectInfo(project: Project) {
        consoleIO.showWithLine(
            """
        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
        ┃   ID          : ${project.id}
        ┃  ️  Name        : ${project.name}
        ┃   Description : ${project.description}
        ┃   Created By  : ${project.createdBy}
        ┃   Created At  : ${project.createdAt}
        ┃   Updated At  : ${project.updatedAt}
        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
        """.trimIndent()
        )
    }
}
