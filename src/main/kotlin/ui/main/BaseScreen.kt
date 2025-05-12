package ui.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui.main.consoleIO.ConsoleIO

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

    protected fun showAnimation(message: String, operation: suspend () -> Unit) {
        val loadingJob = CoroutineScope(Dispatchers.Default).launch {
            val frames = listOf("⣾", "⣽", "⣻", "⢿", "⡿", "⣟", "⣯", "⣷")
            var i = 0
            while (isActive) {
                val frame = frames[i % frames.size]
                consoleIO.show("\r\u001B[36m$frame $message\u001B[0m")
                i++
                delay(100)
            }
        }

        runBlocking {
            try {
                operation()
            } finally {
                loadingJob.cancel()
                consoleIO.show("\r" + " ".repeat(message.length + 10) + "\r")
            }
        }
    }
}