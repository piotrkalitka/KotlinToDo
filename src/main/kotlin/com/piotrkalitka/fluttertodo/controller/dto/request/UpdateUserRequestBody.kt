package com.piotrkalitka.fluttertodo.controller.dto.request

data class UpdateUserRequestBody(
    val username: String?,
    val email: String?,
    val password: String?,
    val roles: Set<String>
)
