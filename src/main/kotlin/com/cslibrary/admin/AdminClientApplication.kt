package com.cslibrary.admin

import com.cslibrary.admin.configuration.ServerConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ServerConfiguration::class)
class AdminClientApplication

fun main(args: Array<String>) {
	runApplication<AdminClientApplication>(*args)
}
