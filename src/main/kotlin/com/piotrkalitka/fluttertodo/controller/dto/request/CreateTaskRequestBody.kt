package com.piotrkalitka.fluttertodo.controller.dto.request

import java.time.LocalDateTime

data class CreateTaskRequestBody(
    val title: String,
    val dueDate: LocalDateTime?,
    val parentId: Long?
)
