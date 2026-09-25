package org.ivcode.homeportal.services.models

/**
 * Manifest describing the contents of a backup archive produced by [org.ivcode.homeportal.services.BackupService].
 *
 * The binary image data itself is stored alongside this manifest as separate zip entries; see
 * [org.ivcode.homeportal.services.BackupService] for the entry naming scheme.
 */
public data class BackupManifest(
    val version: Int = 1,
    val links: List<BackupLink>,
    val backgrounds: List<BackupBackground>,
)

public data class BackupLink(
    val name: String,
    val href: String,
    val icon: BackupImageRef?,
)

public data class BackupBackground(
    val filename: String,
    val mime: String,
)

public data class BackupImageRef(
    val filename: String,
    val mime: String,
)
