package com.cslibrary.admin.main

import com.cslibrary.admin.data.SealedUser
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component

@Component
class UserInfoView(
    private val serverModel: ServerModel
) {
    fun getUserList() {
        val sealedUserList: List<SealedUser> = runCatching {
            serverModel.getSealedUserList()
        }.getOrElse {
            println("Error getting sealed user list from server.")
            println("Message: ${it.message}")
            return
        }

        printOut(sealedUserList)
    }

    private fun printOut(sealedUserList: List<SealedUser>) {
        sealedUserList.forEach {
            println("${it}\n")
        }
    }
}