package ui

import logic.factories.ProjectFactory
import logic.factories.StateFactory
import logic.factories.TaskFactory
import ui.service.ConsoleIOService
import ui.serviceImpl.*

class MainMenuController(
    private val authUIService: AuthUIServiceImpl,
    private val projectUIService: ProjectUIServiceImpl,
    private val taskUIService: TaskUIServiceImpl,
    private val stateUIService: StateUIServiceImpl,
    private val auditUIService: AuditUIServiceImpl,
    private val swimlaneUIService: SwimlaneUIServiceImpl,
    private val console: ConsoleIOService,
) {

    fun showMainMenu() {
        if (!authUIService.login()) {
            console.printError("Login failed. Exiting...")
            return
        }

        when (authUIService.getUserRole()) {
            "Admin" -> adminMenuLoop()
            "Mate" -> mateMenuLoop()
            else -> console.printError("Unknown role. Exiting...")
        }
    }

    private fun adminMenuLoop() {
        var running = true
        while (running) {
            console.printDivider()
            console.printMessage(
                """
                Admin Menu:
                1. Manage Projects
                2. Manage Tasks
                3. Manage States
                4. View Audit
                5. View Swimlanes
                6. Manage Users
                7. Logout
                """.trimIndent()
            )
            console.printDivider()

            when (console.readNonEmptyLine("Enter your choice:").trim()) {
                "1" -> manageProjects()
                "2" -> manageTasks()
                "3" -> manageStates()
                "4" -> viewAudit()
                "5" -> viewSwimlanes()
                "6" -> manageUsers()
                "7" -> {
                    console.printMessage("Logging out...")
                    running = false
                }
                else -> console.printError("Invalid option. Try again.")
            }
        }
    }

    private fun mateMenuLoop() {
        var running = true
        while (running) {
            console.printDivider()
            console.printMessage(
                """
                Mate Menu:
                1. Manage Tasks
                2. View Audit
                3. View Swimlanes
                4. Logout
                """.trimIndent()
            )
            console.printDivider()

            when (console.readNonEmptyLine("Enter your choice:").trim()) {
                "1" -> manageTasks()
                "2" -> viewAudit()
                "3" -> viewSwimlanes()
                "4" -> {
                    console.printMessage("Logging out...")
                    running = false
                }
                else -> console.printError("Invalid option. Try again.")
            }
        }
    }

    private fun manageProjects() {
        val choice = console.readNonEmptyLine(
            """
            Project Options:
            1. List Projects
            2. Add Project
            3. Edit Project
            4. Delete Project
            """.trimIndent()
        ).trim()

        when (choice) {
            "1" -> projectUIService.getAllProjects().forEach { console.printMessage(it.toString()) }
            "2" -> {
                val name = console.readNonEmptyLine("Enter Project Name:")
                val description = console.readNonEmptyLine("Enter Project Description:")
                val createdBy = console.readNonEmptyLine("Enter Creator Name:")

                val project = ProjectFactory.create(name, description, createdBy)
                projectUIService.addProject(project)
            }
            "3" -> {
                val id = console.readNonEmptyLine("Enter Project ID to update:")
                projectUIService.updateProject(id)
            }
            "4" -> {
                val id = console.readNonEmptyLine("Enter Project ID to delete:")
                projectUIService.deleteProject(id)
            }

            else -> console.printError("Invalid option.")
        }
    }

    private fun manageTasks() {
        val choice = console.readNonEmptyLine(
            """
            Task Options:
            1. List Tasks
            2. Add Task
            3. Edit Task
            4. Delete Task
            """.trimIndent()
        ).trim()

        when (choice) {
            "1" -> taskUIService.getAllTasks().forEach { console.printMessage(it.toString()) }
            "2" -> {
                val taskId = console.readNonEmptyLine("Enter Project ID:")
                val title = console.readNonEmptyLine("Enter Task Title:")
                val description = console.readNonEmptyLine("Enter Task Description:")
                val createdBy = console.readNonEmptyLine("Enter Creator Name:")
                val stateId = console.readNonEmptyLine("Enter State ID:")
                val task = TaskFactory.create(taskId, title, description, createdBy, stateId)
                taskUIService.addTask(task)
            }
            "3" -> {
                val taskId = console.readNonEmptyLine("Enter Task ID to update:")
                taskUIService.updateTask(taskId)
            }
            "4" -> {
                val taskId = console.readNonEmptyLine("Enter Task ID to delete:")
                taskUIService.deleteTask(taskId)
            }
            else -> console.printError("Invalid option.")
        }
    }

    private fun manageStates() {
        val projectId = console.readNonEmptyLine("Enter Project ID for states:")
        val choice = console.readNonEmptyLine(
            """
            State Options:
            1. List States
            2. Add State
            3. Edit State
            4. Delete State
            """.trimIndent()
        ).trim()

        when (choice) {
            "1" -> stateUIService.getStatesByProjectId(projectId).forEach { console.printMessage(it.toString()) }
            "2" -> {
                val id = console.readNonEmptyLine("Enter State ID:")
                val name = console.readNonEmptyLine("Enter State Name:")
                val projectId = console.readNonEmptyLine("Enter Project ID:")

                val state = StateFactory.create(id, name, projectId)
                stateUIService.addState(state)
            }

            "3" -> {
                val id = console.readNonEmptyLine("Enter State ID to update:")
                val existing = stateUIService.getStateById(id)
                val newName = console.readNonEmptyLine("Enter new state name:")
                val updated = existing.copy(name = newName)
                stateUIService.updateState(updated)
            }
            "4" -> {
                val id = console.readNonEmptyLine("Enter State ID to delete:")
                val state = stateUIService.getStateById(id)
                stateUIService.deleteState(state)
            }

            else -> console.printError("Invalid option.")
        }
    }

    private fun viewAudit() {
        val choice = console.readNonEmptyLine(
            """
            Audit Options:
            1. View All
            2. By Project ID
            3. By Task ID
            """.trimIndent()
        ).trim()

        when (choice) {
            "1" -> auditUIService.getAllAuditLogs().forEach { console.printMessage(it.toString()) }
            "2" -> {
                val pid = console.readNonEmptyLine("Enter Project ID:")
                auditUIService.getAuditLogsByProjectId(pid).forEach { console.printMessage(it.toString()) }
            }
            "3" -> {
                val tid = console.readNonEmptyLine("Enter Task ID:")
                auditUIService.getAuditLogsByTaskId(tid).forEach { console.printMessage(it.toString()) }
            }
            else -> console.printError("Invalid option.")
        }
    }

    private fun viewSwimlanes() {
        val projectId = console.readNonEmptyLine("Enter Project ID to view swimlanes:")
        swimlaneUIService.displaySwimlanes(projectId)
    }

    private fun manageUsers() {
        console.printMessage("User management not implemented yet.")
    }
}
