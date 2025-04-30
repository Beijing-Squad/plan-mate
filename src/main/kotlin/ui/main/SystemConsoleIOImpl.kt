package ui.main

class SystemConsoleIOImpl: ConsoleIO {
    override fun show(message: String) {
        show(message)
    }

    override fun showWithLine(message: String) {
        showWithLine(message)
    }

    override fun read(): String? {
        return readlnOrNull()
    }

}