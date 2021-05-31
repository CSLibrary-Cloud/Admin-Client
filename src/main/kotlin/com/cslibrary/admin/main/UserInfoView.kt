package com.cslibrary.admin.main

import com.cslibrary.admin.data.SealedUser
import com.cslibrary.admin.main.io.MainIO
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
            MainIO.printError(
                """
                    |Error getting sealed user list from server.
                    |Reason: ${it.message}
                """.trimMargin()
            )
            return
        }

        printOut(sealedUserList)
    }

    private fun printOut(sealedUserList: List<SealedUser>) {
        sealedUserList.forEach {
            MainIO.printNormal("${it}\n")
        }
    }
}