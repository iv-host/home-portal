package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.services.models.UpdateLinkRequest
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.services.LinkService
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.ivcode.homeportal.utils.realmRoles
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name="link-controller", description = "operations related to creating, updating, and deleting links")
@RestController
@RequestMapping("/api/links")
public class LinkController(
    private val linkService: LinkService
) {

    @Operation(description = "Lists all links available to the current user")
    @GetMapping
    public fun getUserLinks(authentication: Authentication?): List<Link> {
        val jwt = (authentication as? JwtAuthenticationToken)?.token
        return linkService.getAllLinks(jwt?.realmRoles() ?: emptySet())
    }

    @Operation(description = "Creates a new link")
    @PostMapping (path= ["/{name}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    public fun postApp(
        @PathVariable("name") name: String,
        @RequestParam("href") href: String,
        @RequestParam("icon") multipartFile: MultipartFile? = null,
        @RequestParam("roles", required = false) roles: List<String> = emptyList(),
    ): Link {
        val icon = multipartFile?.takeIf { !it.isEmpty && !it.originalFilename.isNullOrBlank() }?.let {
            CreateImageRequest(
                filename = it.originalFilename!!,
                mime = it.contentType ?: "application/octet-stream",
                data = it.inputStream
            )
        }
        val request = CreateLinkRequest(
            name = name,
            href = href,
            roles = roles,
            icon = icon
        )

        return linkService.createLink(request)
    }

    @Operation(description = "Updates an existing link")
    @PutMapping(path = ["/{oldName}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    public fun putApp(
        @PathVariable("oldName") oldName: String,
        @RequestParam("name") name: String,
        @RequestParam("href") href: String,
        @RequestParam("icon", required = false) multipartFile: MultipartFile? = null,
        @RequestParam("roles", required = false) roles: List<String> = emptyList(),
    ): Link {
        val icon = multipartFile?.takeIf { !it.isEmpty && !it.originalFilename.isNullOrBlank() }?.let {
            CreateImageRequest(
                filename = it.originalFilename!!,
                mime = it.contentType ?: "application/octet-stream",
                data = it.inputStream
            )
        }
        val request = UpdateLinkRequest(
            name = name,
            href = href,
            roles = roles,
            icon = icon
        )

        return linkService.updateLink(oldName, request)
    }

    @Operation(description = "Deletes a link")
    @DeleteMapping(path = ["/{name}"])
    public fun deleteApp(
        @PathVariable name: String
    ) {
        linkService.deleteLink(name)
    }
}