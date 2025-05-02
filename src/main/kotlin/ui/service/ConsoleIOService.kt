package ui.service

interface ConsoleIOService {
    fun printMessage(msg: String)
    fun printError(msg: String)
    fun printDivider()
    fun readLine(prompt: String): String
    fun readNonEmptyLine(prompt: String): String
}