package org.ivcode.homeportal.services.models

public data class UpdateLinkRequest(
    val name: String,
    val href: String,
    val icon: CreateImageRequest?,
    val roles: List<String> = emptyList(),
)
