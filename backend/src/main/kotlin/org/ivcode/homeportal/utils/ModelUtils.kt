package org.ivcode.homeportal.utils

import org.ivcode.homeportal.controllers.IMAGE_CONTROLLER_PATH
import org.ivcode.homeportal.repositories.entities.BackgroundImageEntity
import org.ivcode.homeportal.services.models.ImageMetaData
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import org.ivcode.homeportal.repositories.entities.LinkIconEntity
import org.ivcode.homeportal.services.models.BackgroundImage
import java.net.URLEncoder.encode

public fun LinkIconEntity.toLink(): Link = Link (
    name = this.link?.name ?: throw IllegalStateException(),
    href = this.link.href ?: throw IllegalStateException(),
    icon = this.image?.toImagePath()
)

public fun ImageMetaEntity.toImageMetaData(): ImageMetaData = ImageMetaData (
    mime = this.mime!!,
    path = this.path!!,
    filename = this.filename!!
)

public fun BackgroundImageEntity.toBackgroundImage(): BackgroundImage = BackgroundImage(
    filename = this.image?.filename!!,
    url = this.image.toImagePath()
)

public fun ImageMetaEntity.toImagePath(): String
    = toImagePath(this.path, this.filename!!)

public fun String.encodeUrl(): String = encode(this, Charsets.UTF_8)

public fun toImagePath(path: String?, filename: String): String {
    val finalPath = if(path.isNullOrBlank()) {
        "/"
    } else {
        "${if (path.startsWith('/')) "" else "/"}$path${if (path.endsWith('/')) "" else "/"}"
    }

    return "${IMAGE_CONTROLLER_PATH}${finalPath}${filename.encodeUrl()}"}
