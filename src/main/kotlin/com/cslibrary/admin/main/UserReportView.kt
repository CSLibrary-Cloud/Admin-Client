package com.cslibrary.admin.main

import com.cslibrary.admin.data.ReportData
import com.cslibrary.admin.main.io.MainIO
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component

@Component
class UserReportView(
    private val serverModel: ServerModel,
    private val userStatusView: UserStatusView
) {
    fun enterReport() {
        val userReportList: List<ReportData> = runCatching {
            serverModel.getUserReportList()
        }.getOrElse {
            MainIO.printError(
                """
                   |Cannot get user report.
                   |Reason: ${it.message}
                """.trimMargin()
            )
            return
        }

        // Empty
        if (userReportList.isEmpty()) {
            MainIO.printNormal("There is no report at the moment!")
            return
        }

        // Show them
        for (i in userReportList.indices) {
            MainIO.printNormal("Ticket Number: ${i+1}")
            MainIO.printNormal("${userReportList[i]}\n")
        }

        // Open Ticket
        val tickToOpen: String = MainIO.getInputNormal("Enter ticket number: ")
        val indexNumber: Int = runCatching {
            tickToOpen.toInt()
        }.getOrElse {
            MainIO.printError("Wrong Number input: $tickToOpen")
            return
        }

        // Input Validation
        if (indexNumber <= 0 || indexNumber > userReportList.size) {
            MainIO.printError("Wrong input range.[1 ~ ${userReportList.size}]")
            return
        }

        openTicket(userReportList[indexNumber-1])

    }

    private fun openTicket(targetTicket: ReportData) {
        // Read, Resolve, Ban/Unban, Notify
        // clear screen first
        MainIO.printNormal("Ticket Selected:")
        MainIO.printNormal(targetTicket.toString())

        var input: String = ""
        do {
            printReportMenu()
            input = MainIO.getInputNormal("Input Menu Number: ")

            when (input) {
                "1" -> {
                    requestRemoveReport(targetTicket.reportIdentifier)
                    break
                }
                "2" -> {
                    userStatusView.banUser()
                }
                "3" -> {
                    userStatusView.unbanUser()
                }
                "4" -> {
                }
                "0" -> continue
                else -> MainIO.printError("Wrong input: $input")
            }
        } while (input != "0")
    }

    private fun requestRemoveReport(id: String) {
        runCatching {
            serverModel.removeUserReport(id)
        }.onFailure {
            MainIO.printError(
                """
                |Cannot mark current report as resolved.
                |Reason: ${it.message}
                """.trimMargin()
            )
        }.onSuccess {
            MainIO.printNormal("Successfully mark report $id as resolved.")
        }
    }

    private fun printReportMenu() {
        MainIO.printNormal("1. Mark this ticket as resolved[dismiss].")
        MainIO.printNormal("2. Ban user.")
        MainIO.printNormal("3. Unban user.")
        MainIO.printNormal("4. Notify User")
        MainIO.printNormal("0. Exit")
    }
}