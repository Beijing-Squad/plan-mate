package ui.main

abstract class BaseScreen(
    private val consoleIO: ConsoleIO
)  {
    abstract val id: String
    abstract val name: String

    open fun execute() {
        showOptionService()
        handleFeatureChoice()
    }

    abstract fun showOptionService()

    abstract fun handleFeatureChoice()

    protected fun getInput(): String? {
        return consoleIO.read()?.trim()
    }
}