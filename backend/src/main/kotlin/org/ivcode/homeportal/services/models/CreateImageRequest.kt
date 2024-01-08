package org.ivcode.homeportal.services.models

import java.io.InputStream

public data class CreateImageRequest (
    val filename: String,
    val mime: String,
    val data: InputStream
)