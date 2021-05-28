package com.cslibrary.admin.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("cs-server")
@ConstructorBinding
class ServerConfiguration(
    private val serverHostIp: String,
    private val serverHostPort: String
) {
    val serverFullUrl: String = "http://${serverHostIp}:${serverHostPort}"
}