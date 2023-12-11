package org.ivcode.homeportal.services.models

data class CreateLinkRequest (
    val name: String,
    val href: String,
    val icon: CreateImageRequest?
)