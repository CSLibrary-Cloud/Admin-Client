package com.cslibrary.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdminClientApplication

fun main(args: Array<String>) {
	runApplication<AdminClientApplication>(*args)
}
