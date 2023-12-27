package org.ivcode.homeportal.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException: RuntimeException()

@ResponseStatus(HttpStatus.CONFLICT)
class ConflictException: RuntimeException()

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException: RuntimeException()

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerErrorException: RuntimeException()