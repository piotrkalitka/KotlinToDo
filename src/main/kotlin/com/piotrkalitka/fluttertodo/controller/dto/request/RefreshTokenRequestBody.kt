package com.piotrkalitka.fluttertodo.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestBody(

    @NotBlank
    val refreshToken: String

)
