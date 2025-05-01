package ui.main.consoleIO

class SystemConsoleIOImpl: ConsoleIO {
    override fun show(message: String) {
        print(message)
    }

    override fun showWithLine(message: String) {
        println(message)
    }

    override fun read(): String? {
        return readlnOrNull()
    }

}