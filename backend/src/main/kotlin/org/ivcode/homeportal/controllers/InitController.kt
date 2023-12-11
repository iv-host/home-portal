package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.InitService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class InitController(
    private val initService: InitService
) {
    @GetMapping
    fun getInit() = initService.getInitInfo()
}