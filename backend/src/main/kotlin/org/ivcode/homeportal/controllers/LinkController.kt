package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.services.LinkService
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name="link-controller", description = "operations related to creating and deleting links")
@RestController
@RequestMapping("/api/links")
public class LinkController(
    private val linkService: LinkService
) {

    @Operation(description = "Lists all links available to the current user")
    @GetMapping
    public fun getUserLinks(): List<Link> {
        return linkService.getAllLinks()
    }

    @Operation(description = "Creates a new link")
    @PostMapping (path= ["/{name}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    public fun postApp(
        @PathVariable("name") name: String,
        @RequestParam("href") href: String,
        @RequestParam("icon") multipartFile: MultipartFile? = null,
    ): Link {
        val request = CreateLinkRequest(
            name = name,
            href = href,
            icon = if(multipartFile==null) null else CreateImageRequest (
                filename = multipartFile.originalFilename!!,
                mime = multipartFile.contentType!!,
                data = multipartFile.inputStream
            )
        )

        return linkService.createLink(request)
    }

    @Operation(description = "Deletes a link")
    @DeleteMapping(path = ["/{name}"])
    public fun deleteApp(
        @PathVariable name: String
    ) {
        linkService.deleteLink(name)
    }
}