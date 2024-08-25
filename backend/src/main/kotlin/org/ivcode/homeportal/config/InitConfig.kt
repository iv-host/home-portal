package org.ivcode.homeportal.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class InitConfig {

    /**
     * Creates a version string by reading the contents of the file "/version.txt"
     * located in the same package as the `InitConfig` class.
     *
     * @return The version string read from the file.
     */
    @Bean("version")
    public fun createVersion(): String = InitConfig::class.java.getResource("/version.txt")?.readText() ?: "dev"
}