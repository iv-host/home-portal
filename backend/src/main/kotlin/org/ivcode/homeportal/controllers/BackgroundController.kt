package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.BackgroundService
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLDecoder.decode

@RestController
@RequestMapping("/api/bg")
class BackgroundController(
    private val backgroundService: BackgroundService
) {
    @GetMapping
    fun getBackgroundImages() = backgroundService.getBackgroundImages()

    @GetMapping("/random")
    fun getRandomBackgroundImage() = backgroundService.getRandomBackgroundImage()

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createBackgroundImage(@RequestParam("image") multipartFile: MultipartFile) = backgroundService.createBackgroundImage(CreateImageRequest(
        mime = multipartFile.contentType,
        filename = multipartFile.originalFilename,
        data = multipartFile.inputStream
    ))

    @DeleteMapping("/{filename}")
    fun deleteBackgroundImage(
        @PathVariable filename: String
    ) = backgroundService.deleteBackgroundImage(filename)
}