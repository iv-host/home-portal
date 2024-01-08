package org.ivcode.homeportal.services

import org.ivcode.homeportal.exceptions.InternalServerErrorException
import org.ivcode.homeportal.exceptions.NotFoundException
import org.ivcode.homeportal.services.models.CreateLinkRequest
import org.ivcode.homeportal.services.models.Link
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
    public fun getAllLinks(): List<Link> {
        return linkDao.getLinkIcons().map { l -> l.toLink() }.sortedBy { it.name }
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
            href = request.href
        )
        linkDao.createLink(link)

        return Link (
            name = request.name,
            href = request.href,
            icon = iconPath
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