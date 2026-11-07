package org.ivcode.homeportal.services

import org.ivcode.homeportal.repositories.LinkDao
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import org.ivcode.homeportal.repositories.entities.LinkEntity
import org.ivcode.homeportal.repositories.entities.LinkIconEntity
import org.ivcode.homeportal.services.models.UpdateLinkRequest
import org.ivcode.homeportal.utils.toImagePath
import org.mockito.Mockito
import kotlin.test.Test
import kotlin.test.assertEquals

class LinkServiceTest {

    @Test
    fun `returns unrestricted and matching role links`() {
        val service = serviceWithLinks(
            link("Common"),
            link("Admin only", roles = listOf("admin")),
            link("Admin or user", roles = listOf("admin", "user")),
        )

        val result = service.getAllLinks(setOf("user"))

        assertEquals(listOf("Admin or user", "Common"), result.map { it.name })
    }

    @Test
    fun `returns only unrestricted links when user has no roles`() {
        val service = serviceWithLinks(
            link("Common"),
            link("Admin only", roles = listOf("admin")),
        )

        val result = service.getAllLinks(emptySet())

        assertEquals(listOf("Common"), result.map { it.name })
    }

    @Test
    fun `updates link fields and roles while keeping the existing icon`() {
        val linkDao = Mockito.mock(LinkDao::class.java)
        val image = ImageMetaEntity(id = 5, path = "/link/Original", filename = "icon.png", mime = "image/png")
        val existing = LinkIconEntity(
            link = LinkEntity(id = 1, name = "Original", href = "https://example.com/old"),
            image = image,
        )
        val updatedEntity = LinkEntity(
            imageId = 5,
            name = "Renamed",
            href = "https://example.com/new",
        )
        Mockito.`when`(linkDao.getLinkIcon("Original")).thenReturn(existing)
        Mockito.`when`(linkDao.updateLink("Original", updatedEntity)).thenReturn(1)

        val service = LinkService(linkDao, Mockito.mock(ImageService::class.java))
        val result = service.updateLink(
            "Original",
            UpdateLinkRequest(
                name = "Renamed",
                href = "https://example.com/new",
                icon = null,
                roles = listOf(" user ", "user", ""),
            )
        )

        assertEquals("Renamed", result.name)
        assertEquals("https://example.com/new", result.href)
        assertEquals(image.toImagePath(), result.icon)
        assertEquals(listOf("user"), result.roles)
        Mockito.verify(linkDao).deleteLinkRoles(1)
        Mockito.verify(linkDao).createLinkRoles(1, listOf("user"))
    }

    private fun serviceWithLinks(vararg links: LinkIconEntity): LinkService {
        val linkDao = Mockito.mock(LinkDao::class.java)
        Mockito.`when`(linkDao.getLinkIcons()).thenReturn(links.toList())

        return LinkService(linkDao, Mockito.mock(ImageService::class.java))
    }

    private fun link(name: String, roles: List<String> = emptyList()): LinkIconEntity =
        LinkIconEntity(
            link = LinkEntity(name = name, href = "https://example.com/$name", roles = roles),
        )
}
