package com.cslibrary.admin.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ViewConfiguration {
    @Bean
    fun getScanner(): Scanner = Scanner(System.`in`)
}