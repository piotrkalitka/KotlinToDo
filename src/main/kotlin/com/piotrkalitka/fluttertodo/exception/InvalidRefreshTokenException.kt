package com.piotrkalitka.fluttertodo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidRefreshTokenException: RuntimeException("Invalid refresh token") {
}
