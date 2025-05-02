package ui.serviceImpl

import ui.service.ConsoleIOService

class ConsoleIOServiceImpl : ConsoleIOService {
    override fun printMessage(msg: String) = println(msg)
    override fun printError(msg: String) = System.err.println("ERROR: $msg")
    override fun printDivider() = println("=".repeat(50))
    override fun readLine(prompt: String): String {
        print("$prompt ")
        return readln()
    }
    override fun readNonEmptyLine(prompt: String): String {
        var line: String
        do {
            print("$prompt ")
            line = readln().trim()
        } while (line.isEmpty())
        return line
    }
}