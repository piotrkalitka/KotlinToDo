package com.piotrkalitka.fluttertodo.controller.dto.response

data class RefreshTokenResponse(

    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"

)
