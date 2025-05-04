package ui.enums

import ui.main.MenuOption

enum class AuditBoardOption(override val code: String, override val description: String) : MenuOption {
    LIST_ALL("1", "List All Audit Logs"),
    FOR_PROJECT("2", "Get Audit Logs For Project"),
    FOR_TASK("3", "Get Audit Logs For Task"),
    BACK("0", "Exit to Main Menu")
}
