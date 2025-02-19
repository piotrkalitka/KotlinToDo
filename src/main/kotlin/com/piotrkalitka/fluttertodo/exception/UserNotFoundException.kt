package com.piotrkalitka.fluttertodo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(val userId: Long): RuntimeException("User with id $userId could not be found") {
}
