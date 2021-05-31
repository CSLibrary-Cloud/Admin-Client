package com.cslibrary.admin.main.io

import java.io.Console
import java.util.*

object MainIO {
    private const val resetColor: String = "\u001B[0m"
    private const val redColor: String = "\u001B[31m"
    private const val greenColor: String = "\u001B[32m"

    // Input Type
    private var useConsole: Boolean = false
    private var console: Console? = System.console()
    private lateinit var scanner: Scanner

    fun initIO() {
        if (console == null) {
            printError(
                """
                   Local Console is not detected. Perhaps you are using this program inside of IDE?
                   Use local console instead of IDE's integrated console.
                   Some features will be not available.
                   [i.e color, escape sequence, disabling echo when input password]
                """.trimIndent()
            )
            useConsole = false
            scanner = Scanner(System.`in`)
        } else {
            useConsole = true
        }
    }

    fun getInputNormal(toPrint: String): String {
        return if (useConsole) {
            console!!.readLine(toPrint)
        } else {
            getInputScanner(toPrint)
        }
    }

    fun getInputPassword(toPrint: String): String {
        return if (useConsole) {
            String(console!!.readPassword(toPrint))
        } else {
            getInputScanner(toPrint)
        }
    }

    private fun getInputScanner(toPrint: String): String {
        printNormal(toPrint, false)
        return scanner.nextLine()
    }

    fun printError(message: String) {
        print("${redColor}[Error]: ")
        print(message)
        println(resetColor)
    }

    fun printNormal(message: String, newLine: Boolean = true) {
        print(greenColor)
        print(message)
        if (newLine) {
            println(resetColor)
        } else {
            print(resetColor)
        }
    }
}