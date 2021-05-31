package com.cslibrary.admin.main

import com.cslibrary.admin.main.io.MainIO
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserStatusView(
    private val userInfoView: UserInfoView,
    private val serverModel: ServerModel
) {
    fun banUser() {
        // Show User first
        userInfoView.getUserList()

        // Input ID
        val userId: String = MainIO.getInputNormal("Input User ID to ban: ")

        runCatching {
            serverModel.updateUser(userId, true)
        }.onFailure {
            MainIO.printError(
                """
                    |Error banning user.
                    |Reason: ${it.message}
                """.trimMargin()
            )
        }.onSuccess {
            MainIO.printNormal("Successfully banned user: ${userId}.")
        }
    }

    fun unbanUser() {
        // Show User first
        userInfoView.getUserList()

        // Input ID
        val userId: String = MainIO.getInputNormal("Input User ID to unban: ")

        runCatching {
            serverModel.updateUser(userId, false)
        }.onFailure {
            MainIO.printError(
                """
                    |Error unbanning user.
                    |Reason: ${it.message}
                """.trimMargin()
            )
        }.onSuccess {
            MainIO.printNormal("Successfully unbanned user: ${userId}.")
        }
    }
}