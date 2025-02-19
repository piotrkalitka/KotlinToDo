package com.piotrkalitka.fluttertodo.controller.dto.request

import java.time.LocalDateTime

data class UpdateTaskRequestBody(
    val title: String?,
    val dueDate: LocalDateTime?,
    val completed: Boolean?,
    val parentId: Long?
)
