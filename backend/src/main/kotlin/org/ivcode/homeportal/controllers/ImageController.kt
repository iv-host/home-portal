package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.ivcode.homeportal.services.models.ImagePath
import org.ivcode.homeportal.services.ImageService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.net.URLDecoder.decode

const val IMAGE_CONTROLLER_PATH = "/img"

@RestController
@RequestMapping(IMAGE_CONTROLLER_PATH)
class ImageController (
    private val imageService: ImageService
) {


    @Hidden
    @RequestMapping(method = [RequestMethod.GET], path = ["/**"])
    fun getImage(
        request: HttpServletRequest
    ): ResponseEntity<StreamingResponseBody> {
        val imagePath = getImagePath(request)
        val metaData = imageService.getMetaData(imagePath.path, imagePath.filename)

        val stream = StreamingResponseBody { out ->
            imageService.getImageData(imagePath.path, imagePath.filename, out)
        }

        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(metaData.mime))
            .body(stream)
    }

    private fun getImagePath(request: HttpServletRequest): ImagePath {
        val fullPath = decode(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)!! as String, Charsets.UTF_8)
        val subPath: String = StringUtils.removeStart(fullPath, IMAGE_CONTROLLER_PATH)
        return imageService.parseImagePath(subPath)
    }
}