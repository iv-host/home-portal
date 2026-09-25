package org.ivcode.homeportal.cli

import org.ivcode.homeportal.services.BackupService
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

public sealed interface BackupCommand {
    public val file: Path

    public data class Export(override val file: Path) : BackupCommand

    public data class Restore(override val file: Path) : BackupCommand

    public companion object {
        public const val USAGE: String =
            "Usage: java -jar home-portal.jar backup <export|restore> <file> [--spring.profiles.active=<profile>]"

        public fun parse(args: Array<String>): BackupCommand? {
            val commandArgs = args.filterNot { it.startsWith("--") }
            if (commandArgs.firstOrNull() != "backup") {
                return null
            }
            require(commandArgs.size == 3) { "A backup command and file are required." }

            val file = Path.of(commandArgs[2]).toAbsolutePath().normalize()
            return when (commandArgs[1]) {
                "export" -> Export(file)
                "restore" -> Restore(file)
                else -> throw IllegalArgumentException("Unknown backup command: ${commandArgs[1]}")
            }
        }
    }
}

@Component
public class BackupCli(
    private val backupService: BackupService,
) {
    public fun execute(command: BackupCommand) {
        when (command) {
            is BackupCommand.Export -> export(command.file)
            is BackupCommand.Restore -> restore(command.file)
        }
    }

    private fun export(file: Path) {
        file.parent?.let(Files::createDirectories)
        Files.newOutputStream(file).use(backupService::exportBackup)
        println("Backup exported to $file")
    }

    private fun restore(file: Path) {
        require(Files.isRegularFile(file)) { "Backup file does not exist: $file" }
        Files.newInputStream(file).use(backupService::importBackup)
        println("Backup restored from $file")
    }
}
