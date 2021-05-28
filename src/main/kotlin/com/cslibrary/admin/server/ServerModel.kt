package com.cslibrary.admin.server

import com.cslibrary.admin.configuration.ServerConfiguration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder

@Component
class ServerModel(
    private val serverConfiguration: ServerConfiguration,
    private val serverHelper: ServerHelper
) {
    private var adminToken: String? = null

    private fun getHeader(): HttpHeaders = HttpHeaders().apply {
        add("X-AUTH-TOKEN", adminToken)
    }

    val restTemplate: RestTemplate = RestTemplate()

    // Ban User
    fun updateUser(userId: String, isBan: Boolean) {
        val uri: UriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(serverConfiguration.serverFullUrl)
            .path("/api/v1/admin/user/${userId}")
            .queryParam("ban", isBan)
        val responseEntity: ResponseEntity<String> = serverHelper.getResponseEntityInStringFormat {
            restTemplate.exchange(uri.toUriString(), HttpMethod.POST, HttpEntity<Unit>(getHeader()))
        }
    }
}