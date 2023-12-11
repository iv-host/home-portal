package org.ivcode.homeportal.services

import org.ivcode.homeportal.services.models.InitInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InitService (
    private val backgroundService: BackgroundService,
    private val linkService: LinkService
) {
    @Transactional
    fun getInitInfo() = InitInfo (
        background = backgroundService.getRandomBackgroundImage(),
        links = linkService.getAllLinks()
    )
}