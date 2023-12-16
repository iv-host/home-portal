package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.InitService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class InitController(
    private val initService: InitService,
    @Qualifier("version") private val version: String,
) {
    @GetMapping
    fun getInit() = initService.getInitInfo()

    @GetMapping("/version")
    fun getVersion() = version
}