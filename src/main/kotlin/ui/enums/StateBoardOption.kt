package ui.enums

import ui.util.MenuOption

enum class StateBoardOption(override val code: String, override val description: String) : MenuOption {
    ADD("1", "Add State"),
    DELETE("2", "Delete State"),
    UPDATE("3", "Update State"),
    GET_ALL("4", "Get All States"),
    GET_BY_ID("5", "Get State by ID"),
    GET_BY_PROJECT_ID("6", "Get States by Project ID"),
    BACK("0", "Back to Main Menu")
}
