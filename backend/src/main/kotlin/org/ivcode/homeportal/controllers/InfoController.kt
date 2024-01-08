package org.ivcode.homeportal.controllers

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/info")
public class InfoController(
    @Qualifier("version") private val version: String,
) {
    @GetMapping("/version")
    public fun getVersion(): String = version
}