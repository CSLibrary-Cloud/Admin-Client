package com.cslibrary.admin.main

import com.cslibrary.admin.data.LoginRequest
import com.cslibrary.admin.server.ServerModel
import org.springframework.stereotype.Component
import java.io.Console
import java.util.*

@Component
class LoginView(
    private val serverModel: ServerModel,
    private val inputScanner: Scanner
) {
    fun login() {
        val loginRequest: LoginRequest = createLoginRequest()

        runCatching {
            serverModel.loginUser(loginRequest)
        }.onSuccess {
            println("Succeed to login to server!")
        }.onFailure {
            println("Error: Cannot log-in to server. See error message instead.")
            println("Message: ${it.message}")
        }
    }
    private fun createLoginRequest(): LoginRequest {
        val console: Console? = System.console()
        if (console == null) {
            println("Error: Cannot get system console, thus some features are not available.[i.e disabling echo when input password]")
            print("Input ID: ")
            val id: String = inputScanner.nextLine()

            print("Input PW: ")
            val password: String = inputScanner.nextLine()
            return LoginRequest(
                userId = id,
                userPassword = password
            )
        }

        // Create Login Request
        return LoginRequest(
            userId = console.readLine("Input ID: "),
            userPassword = String(console.readPassword("Input Password: "))
        )
    }
}