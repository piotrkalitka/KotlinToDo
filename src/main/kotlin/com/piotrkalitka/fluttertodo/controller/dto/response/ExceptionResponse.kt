package com.piotrkalitka.fluttertodo.controller.dto.response

import java.time.LocalDateTime

data class ExceptionResponse(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
