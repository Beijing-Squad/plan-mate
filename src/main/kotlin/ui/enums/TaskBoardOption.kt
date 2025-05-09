package ui.enums

enum class TaskBoardOption(val code: String, val description: String) {
    VIEW_TASKS("1", "📊 View Tasks in Swimlanes"),
    ADD_TASK("2", "➕ Add New Task"),
    GET_TASK_BY_ID("3", "🔍 Get Task by ID"),
    DELETE_TASK("4", "❌ Delete Task by ID"),
    BACK("0", "🔙 Back to Main Menu");
}