package org.ivcode.homeportal.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException: RuntimeException()

@ResponseStatus(HttpStatus.CONFLICT)
class ConflictException: RuntimeException()