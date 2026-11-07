package org.ivcode.homeportal.repositories.entities

public data class LinkEntity (
    val id: Long? = null,
    val imageId: Long? = null,
    val name: String? = null,
    val href: String? = null,
    val roles: List<String> = emptyList(),
)