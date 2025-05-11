package ui.screens

import format
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.entities.ActionType
import logic.entities.Audit
import logic.entities.EntityType
import logic.entities.Project
import logic.entities.type.UserRole
import logic.useCases.audit.AddAuditLogUseCase
import logic.useCases.authentication.SessionManagerUseCase
import logic.useCases.project.*
import ui.enums.ProjectBoardOption
import ui.main.BaseScreen
import ui.main.MenuRenderer
import ui.main.consoleIO.ConsoleIO
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

    private val sessionManager = SessionManagerUseCase()
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
            showOptionService()
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
        }
    }

    private fun listAllProjects() = runBlocking{
        try {
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                consoleIO.showWithLine("\u001B[33m⚠️ No projects found.\u001B[0m")
            } else {
                projects.forEach { project ->
                    showProjectInfo(project)
                }
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun findProjectById() = runBlocking{
        try {
            consoleIO.show("\u001B[32mEnter project ID: \u001B[0m")
            val id = getInput() ?: return@runBlocking
            val project = getProjectByIdUseCase.getProjectById(id)
            showProjectInfo(project)
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun updateProject() = runBlocking{
        try {
            consoleIO.show("\u001B[32mEnter project ID to update: \u001B[0m")
            val id = getInput() ?: return@runBlocking
            val project = getProjectByIdUseCase.getProjectById(id)

            consoleIO.show("\u001B[32mEnter new name: \u001B[0m")
            val name = getInput() ?: return@runBlocking
            consoleIO.show("\u001B[32mEnter new description: \u001B[0m")
            val desc = getInput() ?: return@runBlocking

            val updated = project.copy(
                name = name,
                description = desc,
                updatedAt = now
            )

            updateProjectUseCase.updateProject(updated)
            consoleIO.showWithLine("\u001B[32m✅ Project updated successfully.\u001B[0m")

            sessionManager.getCurrentUser()?.userName?.let { userName ->
                val actionDetails = "Admin $userName updated project ${updated.id} with name '$name' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = userRole,
                        userName = userName,
                        action = ActionType.UPDATE,
                        entityType = EntityType.PROJECT,
                        entityId = updated.id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun addProject()  = runBlocking{
        try {
            consoleIO.show("\u001B[32mEnter project name: \u001B[0m")
            val name = getInput() ?: return@runBlocking
            consoleIO.show("\u001B[32mEnter description: \u001B[0m")
            val desc = getInput() ?: return@runBlocking
            consoleIO.show("\u001B[32mEnter created by (user ID): \u001B[0m")
            val createdBy = getInput() ?: return@runBlocking
            val newProject = Project(
                name = name,
                description = desc,
                createdBy = createdBy,
                createdAt = now,
                updatedAt = now
            )

            addProjectUseCase.addProject(newProject)
            consoleIO.showWithLine("\u001B[32m✅ Project added successfully.\u001B[0m")
            sessionManager.getCurrentUser()?.userName?.let { userName ->
                val actionDetails =
                    "Admin $userName created project ${newProject.id} with name '$name' at ${now.format()}"

                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = userRole,
                        userName = createdBy,
                        action = ActionType.CREATE,
                        entityType = EntityType.PROJECT,
                        entityId = newProject.id.toString(),
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
    }

    private fun deleteProject()= runBlocking {
        try {
            consoleIO.show("\u001B[32mEnter project ID to delete: \u001B[0m")
            val id = getInput() ?: return@runBlocking
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            deleteProjectUseCase.deleteProject(id)
            consoleIO.showWithLine("\u001B[32m✅ Project deleted successfully.\u001B[0m")
            sessionManager.getCurrentUser()?.userName?.let { userName ->
                val actionDetails = "Admin $userName deleted project $id with name '$name' at ${now.format()}"
                addAudit.addAuditLog(
                    Audit(
                        id = Uuid.random(),
                        userRole = UserRole.ADMIN,
                        userName = userName,
                        action = ActionType.DELETE,
                        entityType = EntityType.PROJECT,
                        entityId = id,
                        actionDetails = actionDetails,
                        timeStamp = now
                    )
                )
            }
        } catch (e: Exception) {
            consoleIO.showWithLine("\u001B[31m❌ ${e.message}\u001B[0m")
        }
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
