package org.ivcode.homeportal.utils

import org.ivcode.homeportal.controllers.IMAGE_CONTROLLER_PATH
import org.ivcode.homeportal.repositories.entities.BackgroundImageEntity
import org.ivcode.homeportal.services.models.ImageMetaData
import org.ivcode.homeportal.services.models.Link
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import org.ivcode.homeportal.repositories.entities.LinkIconEntity
import org.ivcode.homeportal.services.models.BackgroundImage
import java.net.URLEncoder.encode

fun LinkIconEntity.toLink() = Link (
    name = this.link?.name ?: throw IllegalStateException(),
    href = this.link.href ?: throw IllegalStateException(),
    icon = this.image?.toImagePath()
)

fun ImageMetaEntity.toImageMetaData() = ImageMetaData (
    mime = this.mime!!,
    path = this.path!!,
    filename = this.filename!!
)

fun BackgroundImageEntity.toBackgroundImage() = BackgroundImage(
    filename = this.image?.filename!!,
    url = this.image.toImagePath()
)

fun ImageMetaEntity.toImagePath(): String
    = toImagePath(this.path, this.filename!!)

fun String.encodeUrl(): String = encode(this, Charsets.UTF_8)

fun toImagePath(path: String?, filename: String): String {
    val finalPath = if(path.isNullOrBlank()) {
        "/"
    } else {
        "${if (path.startsWith('/')) "" else "/"}$path${if (path.endsWith('/')) "" else "/"}"
    }

    return "${IMAGE_CONTROLLER_PATH}${finalPath}${filename.encodeUrl()}"}
