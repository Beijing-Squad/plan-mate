package ui.enums

import ui.main.MenuOption

enum class TaskBoardOption(override val code: String, override val description: String) : MenuOption {
    VIEW_TASKS("1", "Show All Tasks (Swimlanes)"),
    ADD_TASK("2", "Add Task"),
    GET_TASK_BY_ID("3", "Find Task by ID"),
    DELETE_TASK("4", "Delete Task"),
    VIEW_TASKS_LIST("5", "Show All Tasks (List View)"),
    UPDATE_TASK("6", "Update Task"),
    BACK("0", "Exit to Main Menu")
}