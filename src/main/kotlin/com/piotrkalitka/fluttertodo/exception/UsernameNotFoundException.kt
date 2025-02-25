package com.piotrkalitka.fluttertodo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UsernameNotFoundException(val username: String): RuntimeException("Username: $username not found") {
}
