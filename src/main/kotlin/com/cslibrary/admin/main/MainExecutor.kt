package com.cslibrary.admin.main

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
    private val userStatusView: UserStatusView
) {
    @PostConstruct
    fun initUI() {
        var menuSelection: MainMenuEntry
        do {
            menuSelection = printMenu()

            when (menuSelection) {
                MainMenuEntry.LOGIN -> {
                    clearScreen()
                    loginView.login()
                }
                MainMenuEntry.USER_INFO -> {
                    clearScreen()
                    userInfoView.getUserList()
                }
                MainMenuEntry.BAN_USER -> {
                    clearScreen()
                    userStatusView.banUser()
                }
                MainMenuEntry.UNBAN_USER -> {}
                MainMenuEntry.USER_REPORT -> {}
                MainMenuEntry.NOTIFY_USER -> {}
                MainMenuEntry.EXIT -> continue
            }

            waitFor()
        } while (menuSelection != MainMenuEntry.EXIT)
    }

    private fun clearScreen() {
        print("\u001B[H\u001B[2J")
        System.out.flush()
    }

    private fun printMenu(): MainMenuEntry {
        clearScreen()
        println("CS-Library Admin's Menu.")
        enumValues<MainMenuEntry>().forEach {
            println(it.toString())
        }
        print("\nInput Menu[Number]: ")

        val rawInput: String = inputScanner.nextLine()

        return convertIntToEnum(
            (convertStringToInt(rawInput) ?: MainMenuEntry.EXIT.ordinal) - 1
        )
    }

    private fun convertStringToInt(input: String): Int? {
        return runCatching {
            input.toInt()
        }.onFailure {
            println("$input cannot be changed to number!")
            println("Please check number you input.")
        }.getOrNull()
    }

    private fun convertIntToEnum(input: Int): MainMenuEntry {
        enumValues<MainMenuEntry>().forEach {
            if (it.ordinal == input) {
                return it
            }
        }

        println("Unknown number ${input + 1}.")
        return MainMenuEntry.EXIT
    }

    private fun waitFor() {
        print("Press Enter to continue..")
        inputScanner.nextLine()
    }
}