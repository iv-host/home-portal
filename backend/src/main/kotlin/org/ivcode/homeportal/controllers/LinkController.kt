package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.services.LinkService
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/links")
class LinkController(
    private val linkService: LinkService
) {

    @GetMapping
    fun getUserLinks(): List<Link> {
        return linkService.getAllLinks()
    }

    @PostMapping (path= ["/{name}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postApp(
        @PathVariable("name") name: String,
        @RequestParam("href") href: String,
        @RequestParam("icon") multipartFile: MultipartFile? = null,
    ): Link {
        val request = CreateLinkRequest(
            name = name,
            href = href,
            icon = if(multipartFile==null) null else CreateImageRequest (
                filename = multipartFile.originalFilename,
                mime = multipartFile.contentType,
                data = multipartFile.inputStream
            )
        )

        return linkService.createLink(request)
    }

    @DeleteMapping(path = ["/{name}"])
    fun deleteApp(
        @PathVariable name: String
    ) {
        linkService.deleteLink(name)
    }
}