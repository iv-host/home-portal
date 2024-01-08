package org.ivcode.homeportal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableTransactionManagement
public class Main

public fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
}