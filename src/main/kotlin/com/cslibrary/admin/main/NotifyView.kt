package com.cslibrary.admin.main

import com.cslibrary.admin.data.NotifyUserRequest
import com.cslibrary.admin.data.UserNotification
import com.cslibrary.admin.main.io.MainIO
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component

@Component
class NotifyView(
    private val serverModel: ServerModel,
    private val userInfoView: UserInfoView
) {
    fun openNotifyMenu() {
        userInfoView.getUserList()
        val notifyUserRequest: NotifyUserRequest = NotifyUserRequest(
            userId = MainIO.getInputNormal("Input target user ID: "),
            userNotification = UserNotification(
                notificationTitle = MainIO.getInputNormal("Input notification title: "),
                notificationMessage = MainIO.getInputNormal("Input notification message[Warning, putting enter will submit its message]: ")
            )
        )

        requestNotify(notifyUserRequest)
    }

    fun openNotifyWithoutPrintingUser() {
        val notifyUserRequest: NotifyUserRequest = NotifyUserRequest(
            userId = MainIO.getInputNormal("Input target user ID: "),
            userNotification = UserNotification(
                notificationTitle = MainIO.getInputNormal("Input notification title: "),
                notificationMessage = MainIO.getInputNormal("Input notification message[Warning, putting enter will submit its message]: ")
            )
        )

        requestNotify(notifyUserRequest)
    }

    private fun requestNotify(notificationUserRequest: NotifyUserRequest) {
        runCatching {
            serverModel.postNotificationToUser(notificationUserRequest)
        }.onFailure {
            MainIO.printError("Cannot post notification to user ${notificationUserRequest.userId}")
            MainIO.printError("Reason: ${it.message}")
        }.onSuccess {
            MainIO.printNormal("Successfully submitted notification to user ${notificationUserRequest.userId}")
        }
    }
}