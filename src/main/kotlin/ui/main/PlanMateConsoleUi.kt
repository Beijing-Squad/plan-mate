package ui.main

import ui.main.consoleIO.ConsoleIO

class PlanMateConsoleUi(
    private val screens: List<BaseScreen>,
    private val consoleIO: ConsoleIO
) : PlanMateUi {
    override fun start() {

        try {
            showWelcome()
            runMenuLoop()
            presentFeatures()
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unknown error occurred"
            consoleIO.showWithLine(errorMessage)
        }
    }

    private fun showWelcome() {
        consoleIO.show("Welcome to Plan Mate\uD83E\uDD6A ")
    }


    private fun runMenuLoop() {
        var running = true
        while (running) {
            presentFeatures()
            val input = consoleIO.read()
            input?.let { running = processInput(it) }
        }
    }

    private fun presentFeatures() {
        consoleIO.showWithLine("\n=== Please select an option ===")
        screens.forEach { feature ->
            consoleIO.showWithLine("${feature.id}. ${feature.name}")
        }
        consoleIO.showWithLine("0. Exit")
        consoleIO.show("Enter choice: ")
    }

    private fun processInput(input: String): Boolean {
        if (input == "0") {
            return false
        }
        val feature = screens.find { it.id == input }
        return if (feature != null) {
            feature.execute()
            true
        } else {
            consoleIO.showWithLine("❌ Invalid input! Please choose one of the available options.")
            true
        }
    }

}
