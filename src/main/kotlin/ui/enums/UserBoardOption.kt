package ui.enums

import ui.main.MenuOption

enum class UserBoardOption(override val code: String, override val description: String) : MenuOption {
    LIST_ALL("1", "List All Users"),
    FIND_BY_ID("2", "Find User by ID"),
    UPDATE("3", "Update User"),
    BACK("0", "Exit to Main Menu")
}
