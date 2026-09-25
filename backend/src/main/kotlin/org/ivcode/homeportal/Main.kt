package org.ivcode.homeportal

import org.ivcode.homeportal.cli.BackupCli
import org.ivcode.homeportal.cli.BackupCommand
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.WebApplicationType
import org.springframework.transaction.annotation.EnableTransactionManagement
import kotlin.system.exitProcess

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableTransactionManagement
public class Main

public fun main(args: Array<String>) {
    val command = try {
        BackupCommand.parse(args)
    } catch (exception: IllegalArgumentException) {
        System.err.println(exception.message)
        System.err.println(BackupCommand.USAGE)
        exitProcess(2)
    }

    if (command == null) {
        SpringApplication.run(Main::class.java, *args)
        return
    }

    val springArgs = args.filter { it.startsWith("--") }.toTypedArray()
    SpringApplicationBuilder(Main::class.java)
        .web(WebApplicationType.NONE)
        .run(*springArgs)
        .use { context ->
            context.getBean(BackupCli::class.java).execute(command)
        }
}