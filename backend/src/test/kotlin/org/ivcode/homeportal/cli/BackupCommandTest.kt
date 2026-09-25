package org.ivcode.homeportal.cli

import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

public class BackupCommandTest {

    @Test
    public fun `returns null for normal server arguments`() {
        assertNull(BackupCommand.parse(arrayOf("--spring.profiles.active=postgres")))
    }

    @Test
    public fun `parses export with Spring arguments`() {
        val command = BackupCommand.parse(
            arrayOf("backup", "export", "backup.zip", "--spring.profiles.active=postgres")
        )

        assertEquals(
            BackupCommand.Export(Path("backup.zip").absolute().normalize()),
            command,
        )
    }

    @Test
    public fun `parses restore with Spring arguments before command`() {
        val command = BackupCommand.parse(
            arrayOf("--spring.profiles.active=postgres", "backup", "restore", "backup.zip")
        )

        assertEquals(
            BackupCommand.Restore(Path("backup.zip").absolute().normalize()),
            command,
        )
    }

    @Test
    public fun `rejects incomplete commands`() {
        assertFailsWith<IllegalArgumentException> {
            BackupCommand.parse(arrayOf("backup", "export"))
        }
    }
}
