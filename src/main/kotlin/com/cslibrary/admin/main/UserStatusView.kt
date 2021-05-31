package com.cslibrary.admin.main

import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserStatusView(
    private val userInfoView: UserInfoView,
    private val inputScanner: Scanner,
    private val serverModel: ServerModel
) {
    fun banUser() {
        // Show User first
        userInfoView.getUserList()

        // Input ID
        println("Input User ID to ban: ")
        val userId: String = inputScanner.nextLine()

        runCatching {
            serverModel.updateUser(userId, true)
        }.onFailure {
            println("Error banning user.")
            println("Reason: ${it.message}")
        }.onSuccess {
            println("Successfully banned user: ${userId}.")
        }
    }
}