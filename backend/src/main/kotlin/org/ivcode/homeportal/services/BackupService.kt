package org.ivcode.homeportal.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.ivcode.homeportal.exceptions.BadRequestException
import org.ivcode.homeportal.repositories.BackgroundDao
import org.ivcode.homeportal.repositories.ImageDao
import org.ivcode.homeportal.repositories.LinkDao
import org.ivcode.homeportal.services.models.BackupBackground
import org.ivcode.homeportal.services.models.BackupImageRef
import org.ivcode.homeportal.services.models.BackupLink
import org.ivcode.homeportal.services.models.BackupManifest
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.utils.encodeUrl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.io.path.deleteIfExists

private const val MANIFEST_ENTRY = "manifest.json"

/**
 * Exports/imports all links and background images (metadata + binary image data) as a single zip
 * archive. Intended for both ad-hoc backups and moving data between database engines (e.g. MySQL ->
 * PostgreSQL): export from the source, switch the datasource, then import into the target.
 *
 * Import is destructive - it replaces all existing links, backgrounds, and images.
 */
@Service
public class BackupService(
    private val linkDao: LinkDao,
    private val backgroundDao: BackgroundDao,
    private val imageDao: ImageDao,
    private val imageService: ImageService,
    private val linkService: LinkService,
    private val backgroundService: BackgroundService,
    private val objectMapper: ObjectMapper,
) {

    @Transactional(readOnly = true)
    public fun exportBackup(out: OutputStream) {
        val links = linkDao.getLinkIcons()
        val backgrounds = backgroundDao.selectBackgroundImages()

        val manifest = BackupManifest(
            links = links.map { linkIcon ->
                val link = linkIcon.link ?: throw IllegalStateException("link missing from link icon row")
                val image = linkIcon.image
                BackupLink(
                    name = link.name ?: throw IllegalStateException("link name is null"),
                    href = link.href ?: throw IllegalStateException("link href is null"),
                    icon = if (image?.filename != null && image.mime != null) {
                        BackupImageRef(filename = image.filename, mime = image.mime)
                    } else null,
                )
            },
            backgrounds = backgrounds.mapNotNull { bg ->
                val image = bg.image ?: return@mapNotNull null
                BackupBackground(
                    filename = image.filename ?: throw IllegalStateException("background image filename is null"),
                    mime = image.mime ?: throw IllegalStateException("background image mime is null"),
                )
            },
        )

        ZipOutputStream(out).use { zip ->
            // ObjectMapper.writeValue(OutputStream, ...) closes the stream it's given when done, which
            // would prematurely close the zip - write to a byte array instead.
            zip.putNextEntry(ZipEntry(MANIFEST_ENTRY))
            zip.write(objectMapper.writeValueAsBytes(manifest))
            zip.closeEntry()

            links.forEach { linkIcon ->
                val link = linkIcon.link ?: return@forEach
                val image = linkIcon.image
                if (image?.path != null && image.filename != null) {
                    zip.putNextEntry(ZipEntry(linkIconEntryName(link.name!!, image.filename)))
                    imageService.getImageData(image.path, image.filename, zip)
                    zip.closeEntry()
                }
            }

            backgrounds.forEach { bg ->
                val image = bg.image
                if (image?.path != null && image.filename != null) {
                    zip.putNextEntry(ZipEntry(backgroundEntryName(image.filename)))
                    imageService.getImageData(image.path, image.filename, zip)
                    zip.closeEntry()
                }
            }
        }
    }

    /**
     * Replaces all links, backgrounds, and images with the contents of the given backup archive.
     */
    @Transactional
    public fun importBackup(input: InputStream) {
        // Buffered to a temp file so entries can be looked up by name in any order (ZipFile requires
        // random access; the uploaded stream itself is not seekable).
        val tempFile = Files.createTempFile("home-portal-backup", ".zip")
        try {
            Files.newOutputStream(tempFile).use { input.copyTo(it) }

            ZipFile(tempFile.toFile()).use { zip ->
                val manifestEntry = zip.getEntry(MANIFEST_ENTRY)
                    ?: throw BadRequestException()
                val manifest = zip.getInputStream(manifestEntry).use {
                    objectMapper.readValue(it, BackupManifest::class.java)
                }

                // Wipe existing data first. Order matters: background/link reference image via a
                // restricting foreign key, so they must be cleared before image.
                backgroundDao.deleteAllBackgrounds()
                linkDao.deleteAllLinks()
                imageDao.deleteAllImages()

                manifest.links.forEach { link -> restoreLink(zip, link) }
                manifest.backgrounds.forEach { background -> restoreBackground(zip, background) }
            }
        } finally {
            tempFile.deleteIfExists()
        }
    }

    private fun restoreLink(zip: ZipFile, link: BackupLink) {
        val icon = link.icon?.let { ref ->
            val entry = zip.getEntry(linkIconEntryName(link.name, ref.filename)) ?: throw BadRequestException()
            CreateImageRequest(filename = ref.filename, mime = ref.mime, data = zip.getInputStream(entry))
        }

        linkService.createLink(CreateLinkRequest(name = link.name, href = link.href, icon = icon))
    }

    private fun restoreBackground(zip: ZipFile, background: BackupBackground) {
        val entry = zip.getEntry(backgroundEntryName(background.filename)) ?: throw BadRequestException()
        val request = CreateImageRequest(
            filename = background.filename,
            mime = background.mime,
            data = zip.getInputStream(entry),
        )

        backgroundService.createBackgroundImage(request)
    }

    private fun linkIconEntryName(name: String, filename: String): String =
        "links/${name.encodeUrl()}/${filename.encodeUrl()}"

    private fun backgroundEntryName(filename: String): String =
        "backgrounds/${filename.encodeUrl()}"
}
