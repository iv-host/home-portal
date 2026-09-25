package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.ivcode.homeportal.services.BackupService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@Tag(name = "backup-controller", description = "export and import all links and background images as a single zip archive")
@RestController
@RequestMapping("/api/backup")
public class BackupController(
    private val backupService: BackupService
) {

    @Operation(description = "Exports all links and background images (including binary image data) as a zip archive")
    @GetMapping
    public fun exportBackup(): ResponseEntity<StreamingResponseBody> {
        val stream = StreamingResponseBody { out -> backupService.exportBackup(out) }

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"home-portal-backup.zip\"")
            .body(stream)
    }

    @Operation(description = "Restores all links and background images from a zip archive previously produced by the export endpoint. This DELETES all existing links, backgrounds, and images.")
    @PostMapping("/restore", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    public fun importBackup(@RequestParam("file") file: MultipartFile) {
        backupService.importBackup(file.inputStream)
    }
}
