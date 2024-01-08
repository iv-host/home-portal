package org.ivcode.homeportal.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class InitConfig {

    @Bean("version")
    public fun createVersion(): String = InitConfig::class.java.getResource("/version.txt").readText()
}