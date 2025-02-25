package com.piotrkalitka.fluttertodo.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtUtils {

    val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    @Value("\${app.jwtSecret}")
    lateinit var jwtSecret: String

    @Value("\${app.jwtExpirationMs}")
    var jwtExpirationMs: Int = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateTokenFromUsername(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key())
            .compact()
    }

    fun getUserNameFromJwtToken(token: String): String = Jwts
        .parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .body
        .subject

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: $authToken")
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT token: $authToken")
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT token: $authToken")
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string empty: $authToken")
        } catch (e: SignatureException) {
            logger.error("Jwt signature is invalid: $authToken")
        }

        return false
    }

    private fun key(): Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

}
