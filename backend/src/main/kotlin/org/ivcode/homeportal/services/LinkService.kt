package org.ivcode.homeportal.services

import org.ivcode.homeportal.exceptions.InternalServerErrorException
import org.ivcode.homeportal.exceptions.NotFoundException
import org.ivcode.homeportal.exceptions.ConflictException
import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.services.models.UpdateLinkRequest
import org.ivcode.homeportal.repositories.LinkDao
import org.ivcode.homeportal.repositories.entities.LinkEntity
import org.ivcode.homeportal.utils.toImagePath
import org.ivcode.homeportal.utils.toLink
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
public class LinkService (
    private val linkDao: LinkDao,
    private val imageService: ImageService,
) {

    @Transactional
    public fun getAllLinks(userRoles: Set<String>): List<Link> {
        return linkDao.getLinkIcons().map { l ->
            l.toLink().copy(roles = l.link?.roles ?: emptyList())
        }.filter { link ->
            link.roles.isEmpty() || link.roles.any(userRoles::contains)
        }.sortedBy { it.name }
    }

    @Transactional
    public fun createLink(request: CreateLinkRequest): Link {
        val path = "/link/${request.name}"
        val iconPath = if (request.icon!=null) toImagePath(
            path,
            request.icon.filename
        ) else null
        val imageId = if (request.icon!=null) imageService.postImage(
            path = path,
            filename = request.icon.filename,
            mime = request.icon.mime,
            input = request.icon.data
        ) else null

        val link = LinkEntity(
            imageId = imageId,
            name = request.name,
            href = request.href,
            roles = request.roles.map(String::trim).filter(String::isNotEmpty).distinct()
        )
        linkDao.createLink(link)
        if (link.roles.isNotEmpty()) {
            linkDao.createLinkRoles(link.id!!, link.roles)
        }

        return Link (
            name = request.name,
            href = request.href,
            icon = iconPath,
            roles = link.roles
        )
    }

    @Transactional
    public fun updateLink(oldName: String, request: UpdateLinkRequest): Link {
        val existing = linkDao.getLinkIcon(oldName) ?: throw NotFoundException()
        val existingLink = existing.link ?: throw InternalServerErrorException()
        val linkId = existingLink.id ?: throw InternalServerErrorException()

        if (request.name != oldName && linkDao.getLinkIcon(request.name) != null) {
            throw ConflictException()
        }

        val roles = request.roles.map(String::trim).filter(String::isNotEmpty).distinct()
        val existingImage = existing.image
        var imageId = existingImage?.id ?: existingLink.imageId
        var iconPath = existingImage?.toImagePath()

        if (request.icon != null) {
            if (existingImage != null) {
                if (linkDao.clearLinkImage(oldName) != 1) {
                    throw InternalServerErrorException()
                }
                imageService.deleteImage(existingImage.path!!, existingImage.filename!!)
            }

            val path = "/link/${request.name}"
            imageId = imageService.postImage(
                path = path,
                filename = request.icon.filename,
                mime = request.icon.mime,
                input = request.icon.data
            )
            iconPath = toImagePath(path, request.icon.filename)
        }

        val updatedLink = LinkEntity(
            imageId = imageId,
            name = request.name,
            href = request.href,
        )
        if (linkDao.updateLink(oldName, updatedLink) != 1) {
            throw InternalServerErrorException()
        }

        linkDao.deleteLinkRoles(linkId)
        if (roles.isNotEmpty()) {
            linkDao.createLinkRoles(linkId, roles)
        }

        return Link(
            name = request.name,
            href = request.href,
            icon = iconPath,
            roles = roles
        )
    }

    @Transactional
    public fun deleteLink(name: String) {
        val linkIcon = linkDao.getLinkIcon(name) ?: throw NotFoundException()

        val count = linkDao.deleteLink(name)
        if(count!=1) {
           throw InternalServerErrorException()
        }

        if(linkIcon.image!=null) {
            imageService.deleteImage(linkIcon.image.path!!, linkIcon.image.filename!!)
        }
    }
}