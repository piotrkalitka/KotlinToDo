package com.piotrkalitka.fluttertodo.controller.dto.response

data class JwtResponse(
    val accessToken: String,
    val type: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)
