package org.ivcode.homeportal.services

import org.ivcode.homeportal.exceptions.ConflictException
import org.ivcode.homeportal.exceptions.NotFoundException
import org.ivcode.homeportal.services.models.ImageMetaData
import org.ivcode.homeportal.services.models.ImagePath
import org.ivcode.homeportal.repositories.ImageDao
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import org.ivcode.homeportal.utils.toImageMetaData
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.io.OutputStream

@Service
public class ImageService (
    private val imageDao: ImageDao
) {

    @Transactional
    public fun getMetaData(path: String, filename: String): ImageMetaData =
        imageDao.getMetaData(path, filename)?.toImageMetaData() ?: throw NotFoundException()

    @Transactional
    public fun getImageData(path: String, filename: String, out: OutputStream) {
        imageDao.getData(path, filename)?.use { it.transferTo(out) } ?: throw NotFoundException()
    }

    @Transactional
    public fun postImage(path: String, filename: String, mime: String, input: InputStream): Long {
        val value = ImageMetaEntity(
            path = path,
            filename = filename,
            mime = mime
        )

        try {
            imageDao.createImage(value, input)
        } catch (e: DuplicateKeyException) {
            throw ConflictException()
        }

        return value.id!!
    }

    @Transactional
    public fun deleteImage(path: String, filename: String) {
        val count = imageDao.deleteImage(path, filename)
        if(count==0) {
            throw NotFoundException()
        }
    }

    @Transactional
    public fun deleteImages(imageIds: List<Long>): Int =
        imageDao.deleteImagesById(imageIds)

    public fun parseImagePath(urlPath: String): ImagePath {
        val index = urlPath.lastIndexOf('/')
        return if(index==-1) {
            ImagePath(
                urlPath = urlPath,
                path = "/",
                filename = urlPath,
            )
        } else {
            val path = urlPath.substring(startIndex = 0, endIndex = index)
            val filename = urlPath.substring(startIndex = index + 1)

            ImagePath(
                urlPath = urlPath,
                path = if(path.startsWith('/')) path else "/${path}",
                filename = filename,
            )
        }
    }
}
