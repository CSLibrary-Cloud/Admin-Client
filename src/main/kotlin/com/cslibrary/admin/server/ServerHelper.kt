package com.cslibrary.admin.server

import com.cslibrary.admin.error.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException

@Component
class ServerHelper {

    // Jackson Object Mapper
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    // getError
    private fun getErrorMessageFromCommunicationException(httpStatusCodeException: HttpStatusCodeException): String {
        // With 4xx & 5xx Codes!
        val body: String = httpStatusCodeException.responseBodyAsString

        // meaning error!
        val errorResponse: ErrorResponse = runCatching {
            objectMapper.readValue<ErrorResponse>(body)
        }.getOrElse {
            // Even converting to ErrorResponse failed
            return "Exception Occurred! - " + (it.message ?: "No message available")
        }

        return "Server responded with: ${errorResponse.statusCode} - ${errorResponse.statusMessage}\nMessage is: ${errorResponse.errorMessage}"
    }

    /**
     * Get Response from server, in "String" Format.
     * About I/O Error.. nothing will be happen rather than printing errors.
     * Calls handleServerClientError when 400 ~ 500 error occurs.
     */
    fun getResponseEntityInStringFormat(toExecute: () -> ResponseEntity<String>): ResponseEntity<String> {
        return runCatching {
            toExecute()
        }.getOrElse {
            val errorMessage: String = when (it) {
                is ResourceAccessException -> "I/O Error Occurred when connecting to a server."
                is HttpClientErrorException, is HttpServerErrorException -> getErrorMessageFromCommunicationException(it as HttpStatusCodeException)
                else -> "Unknown Error Occurred. Call developer with below logs.\n ${it.stackTraceToString()}"
            }
            throw RuntimeException("Server Communication Failed: $errorMessage")
        }
    }

    final inline fun <reified T> getObjectValues(body: String): T {
        return runCatching {
            objectMapper.readValue<T>(body)
        }.getOrElse {
            throw RuntimeException("Error when changing response body to object: ${it.stackTraceToString()}")
        }
    }
}