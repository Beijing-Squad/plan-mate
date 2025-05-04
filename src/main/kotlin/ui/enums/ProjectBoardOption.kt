package ui.enums

enum class ProjectBoardOption(val code: String, val description: String) {
    LIST_ALL("1", "📋 List All Projects"),
    FIND_BY_ID("2", "🔍 Find Project by ID"),
    UPDATE("3", "✏️ Update Project"),
    ADD("4", "➕ Add Project"),
    DELETE("5", "❌ Delete Project"),
    BACK("0", "🔙 Exit to Main Menu")
}
