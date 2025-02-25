package com.piotrkalitka.fluttertodo.jpa.repository

import com.piotrkalitka.fluttertodo.jpa.entity.RefreshToken
import com.piotrkalitka.fluttertodo.jpa.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User)
    fun existsByUser(user: User): Boolean
}
