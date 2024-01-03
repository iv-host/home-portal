package org.ivcode.homeportal.services.models

data class UserInfo(
    val features: Features
)

data class Features (
    val settings: Boolean
)
