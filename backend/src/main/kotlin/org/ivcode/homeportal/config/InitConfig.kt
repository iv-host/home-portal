package org.ivcode.homeportal.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InitConfig {

    @Bean("version")
    fun createVersion(): String = InitConfig::class.java.getResource("/version.txt").readText()
}