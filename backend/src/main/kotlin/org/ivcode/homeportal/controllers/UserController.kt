package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getUserInfo() = userService.getUserInfo()
}