package com.piotrkalitka.fluttertodo.service

import com.piotrkalitka.fluttertodo.exception.InvalidRefreshTokenException
import com.piotrkalitka.fluttertodo.exception.RefreshTokenExpiredException
import com.piotrkalitka.fluttertodo.jpa.entity.RefreshToken
import com.piotrkalitka.fluttertodo.jpa.entity.User
import com.piotrkalitka.fluttertodo.jpa.repository.RefreshTokenRepository
import com.piotrkalitka.fluttertodo.jpa.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService @Autowired constructor(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
) {

    @Value("\${app.jwtRefreshExpirationMs}")
    private var refreshTokenDuration: Long = 0;

    fun createRefreshToken(user: User): RefreshToken {
        val refreshToken = RefreshToken(
            id = null,
            token = UUID.randomUUID().toString(),
            expiryDate = Instant.now().plusMillis(refreshTokenDuration),
            user = user
        )
        return refreshTokenRepository.save(refreshToken)
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate.isBefore(Instant.now())) {
            refreshTokenRepository.delete(token)
            throw RefreshTokenExpiredException()
        }
        return token
    }

    fun getByToken(token: String): RefreshToken {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow { InvalidRefreshTokenException() }
    }

    fun existsByUser(user: User): Boolean {
        return refreshTokenRepository.existsByUser(user)
    }

    @Transactional
    fun deleteByUser(user: User) {
        refreshTokenRepository.deleteByUser(user)
    }

}
