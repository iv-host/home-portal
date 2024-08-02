package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.BackgroundService
import org.ivcode.homeportal.services.models.BackgroundImage
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/bg")
public class BackgroundController(
    private val backgroundService: BackgroundService
) {
    @GetMapping
    public fun getBackgroundImages(): List<BackgroundImage> = backgroundService.getBackgroundImages()

    @GetMapping("/random")
    public fun getRandomBackground(): BackgroundImage = backgroundService.getRandomBackgroundImage()

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    public fun createBackgroundImage(@RequestParam("image") multipartFile: MultipartFile): BackgroundImage = backgroundService.createBackgroundImage(CreateImageRequest(
        mime = multipartFile.contentType!!,
        filename = multipartFile.originalFilename!!,
        data = multipartFile.inputStream
    ))

    @DeleteMapping("/{filename}")
    public fun deleteBackgroundImage(
        @PathVariable filename: String
    ) {
        backgroundService.deleteBackgroundImage(filename)
    }
}