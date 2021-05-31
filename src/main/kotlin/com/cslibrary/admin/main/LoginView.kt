package com.cslibrary.admin.main

import com.cslibrary.admin.data.LoginRequest
import com.cslibrary.admin.main.io.MainIO
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component

@Component
class LoginView(
    private val serverModel: ServerModel
) {
    fun login() {
        val loginRequest: LoginRequest = createLoginRequest()

        runCatching {
            serverModel.loginUser(loginRequest)
        }.onSuccess {
            MainIO.printNormal("Succeed to login to server!")
        }.onFailure {
            MainIO.printError(
                """
                    |Error: Cannot log-in to server. See error message instead.
                    |Message: ${it.message}
                """.trimMargin()
            )
        }
    }
    private fun createLoginRequest(): LoginRequest {
        // Create Login Request
        return LoginRequest(
            userId = MainIO.getInputNormal("Input ID: "),
            userPassword = MainIO.getInputPassword("Input Password: ")
        )
    }
}