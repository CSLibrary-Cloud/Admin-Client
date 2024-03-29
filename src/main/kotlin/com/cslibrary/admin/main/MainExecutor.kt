package com.cslibrary.admin.main

import com.cslibrary.admin.main.io.MainIO
import com.cslibrary.admin.main.io.MainIO.clearScreen
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

// Menu Entry
enum class MainMenuEntry(private val menuName: String) {
    LOGIN("Login[Admin]"),
    USER_INFO("See Registered User Information"),
    BAN_USER("Ban User"),
    UNBAN_USER("Unban User"),
    USER_REPORT("See User's Report"),
    NOTIFY_USER("Send notification to user"),
    EXIT("Exit");

    override fun toString(): String {
        return "${ordinal + 1}. $menuName"
    }
}

@Component
class MainExecutor(
    private val inputScanner: Scanner,
    private val loginView: LoginView,
    private val userInfoView: UserInfoView,
    private val userStatusView: UserStatusView,
    private val userReportView: UserReportView,
    private val notifyView: NotifyView
) {
    @PostConstruct
    fun initUI() {
        // Init I/O
        MainIO.initIO()

        var menuSelection: MainMenuEntry
        do {
            menuSelection = printMenu()
            clearScreen()

            when (menuSelection) {
                MainMenuEntry.LOGIN -> loginView.login()
                MainMenuEntry.USER_INFO -> userInfoView.getUserList()
                MainMenuEntry.BAN_USER -> userStatusView.banUser()
                MainMenuEntry.UNBAN_USER -> userStatusView.unbanUser()
                MainMenuEntry.USER_REPORT -> userReportView.enterReport()
                MainMenuEntry.NOTIFY_USER -> notifyView.openNotifyMenu()
                MainMenuEntry.EXIT -> continue
            }

            waitFor()
        } while (menuSelection != MainMenuEntry.EXIT)
    }

    private fun printMenu(): MainMenuEntry {
        clearScreen()
        println("CS-Library Admin's Menu.")
        enumValues<MainMenuEntry>().forEach {
            MainIO.printNormal(it.toString())
        }
        MainIO.printNormal("\nInput Menu[Number]: ", false)

        val rawInput: String = inputScanner.nextLine()

        return convertIntToEnum(
            (convertStringToInt(rawInput) ?: MainMenuEntry.EXIT.ordinal) - 1
        )
    }

    private fun convertStringToInt(input: String): Int? {
        return runCatching {
            input.toInt()
        }.onFailure {
            MainIO.printError("$input cannot be changed to number!\nPlease check number you input.")
        }.getOrNull()
    }

    private fun convertIntToEnum(input: Int): MainMenuEntry {
        enumValues<MainMenuEntry>().forEach {
            if (it.ordinal == input) {
                return it
            }
        }

        MainIO.printError("Unknown number ${input + 1}.")
        return MainMenuEntry.EXIT
    }

    private fun waitFor() {
        MainIO.printNormal("Press Enter to continue..", false)
        inputScanner.nextLine()
    }
}