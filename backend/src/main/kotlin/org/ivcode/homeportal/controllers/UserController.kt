package org.ivcode.homeportal.controllers

import org.ivcode.homeportal.services.UserService
import org.ivcode.homeportal.services.models.UserInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
public class UserController(
    private val userService: UserService
) {
    @GetMapping
    public fun getUserInfo(): UserInfo = userService.getUserInfo()
}