package org.ivcode.homeportal.services

import org.ivcode.homeportal.exceptions.NotFoundException
import org.ivcode.homeportal.repositories.BackgroundDao
import org.ivcode.homeportal.repositories.entities.BackgroundEntity
import org.ivcode.homeportal.services.models.BackgroundImage
import org.ivcode.homeportal.services.models.CreateImageRequest
import org.ivcode.homeportal.utils.toBackgroundImage
import org.ivcode.homeportal.utils.toImagePath
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val IMAGE_PATH = "/bg"

@Service
public class BackgroundService(
    private val backgroundDao: BackgroundDao,
    private val imageService: ImageService
) {

    @Transactional
    public fun getBackgroundImages(): List<BackgroundImage> = backgroundDao.selectBackgroundImages().map { bg -> bg.toBackgroundImage() }

    @Transactional
    public fun getRandomBackgroundImage(): BackgroundImage = getBackgroundImages().randomOrNull() ?: throw NotFoundException()

    @Transactional
    public fun createBackgroundImage(request: CreateImageRequest): BackgroundImage {
        val imageId = imageService.postImage(
            mime = request.mime,
            path = IMAGE_PATH,
            filename = request.filename,
            input = request.data
        )

        backgroundDao.createBackgroundImage(BackgroundEntity(
            imageId = imageId
        ))

        return BackgroundImage(
            filename = request.filename,
            url = toImagePath(IMAGE_PATH, request.filename)
        )
    }

    @Transactional
    public fun deleteBackgroundImage(filename: String) {
        val count = backgroundDao.deleteBackgroundImage(filename)
        if(count==0) {
            throw NotFoundException()
        }

        imageService.deleteImage(IMAGE_PATH, filename)
    }
}