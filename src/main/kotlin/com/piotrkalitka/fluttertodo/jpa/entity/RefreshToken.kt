package com.piotrkalitka.fluttertodo.jpa.entity

import org.springframework.data.annotation.Id
import org.springframework.data.geo.Distance
import org.springframework.data.redis.core.RedisHash
import java.time.Instant

@RedisHash("RefreshToken")
data class RefreshToken(

    @Id
    val id: Long?,

    val user: User,

    val token: String,

    val expiryDate: Instant

)
