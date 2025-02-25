package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.controller.dto.request.LoginRequest
import com.piotrkalitka.fluttertodo.controller.dto.request.RefreshTokenRequestBody
import com.piotrkalitka.fluttertodo.controller.dto.request.RegisterRequest
import com.piotrkalitka.fluttertodo.controller.dto.response.JwtResponse
import com.piotrkalitka.fluttertodo.exception.UsernameNotFoundException
import com.piotrkalitka.fluttertodo.jpa.entity.ERole
import com.piotrkalitka.fluttertodo.jpa.entity.Role
import com.piotrkalitka.fluttertodo.jpa.entity.User
import com.piotrkalitka.fluttertodo.jpa.repository.RoleRepository
import com.piotrkalitka.fluttertodo.jpa.repository.UserRepository
import com.piotrkalitka.fluttertodo.security.JwtUtils
import com.piotrkalitka.fluttertodo.security.UserDetailsImpl
import com.piotrkalitka.fluttertodo.service.RefreshTokenService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val encoder: PasswordEncoder,
    val jwtUtils: JwtUtils,
    val refreshTokenService: RefreshTokenService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl
        val user = userRepository.findByUsername(userDetails.username)
            .orElseThrow { UsernameNotFoundException(userDetails.username) }

        if (refreshTokenService.existsByUser(user)) {
            refreshTokenService.deleteByUser(user)
        }

        val refreshToken = refreshTokenService.createRefreshToken(user)
        val roles = getUserRoles(userDetails)


        return ResponseEntity.ok(
            JwtResponse(
                accessToken = jwt,
                refreshToken = refreshToken.token,
                id = userDetails.id,
                username = userDetails.username,
                email = userDetails.email,
                roles = roles
            )
        )
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        if (userRepository.existsByEmail(request.email))
            return ResponseEntity
                .badRequest()
                .body("Email address taken")
        if (userRepository.existsByUsername(request.username))
            return ResponseEntity
                .badRequest()
                .body("Username taken")

        val user = User(request.username, request.email, encoder.encode(request.password))
        val roles = mutableSetOf<Role>(
            roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow { RuntimeException("User role could not be found") }
        )

        user.roles = roles
        val persistedUser = userRepository.save(user)
        return ResponseEntity.status(201).body(persistedUser)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequestBody): ResponseEntity<JwtResponse> {
        val refreshToken = refreshTokenService.getByToken(request.refreshToken)
        val validToken = refreshTokenService.verifyExpiration(refreshToken)

        val user = validToken.user
        val accessToken = jwtUtils.generateTokenFromUsername(user.username)

        val roles = user.roles
            .map { role: Role -> role.name.name }

        return ResponseEntity.ok(JwtResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            roles = roles,
            accessToken = accessToken,
            refreshToken = request.refreshToken
        ))
    }

    private fun getUserRoles(userDetails: UserDetailsImpl): List<String> {
        return userDetails.authorities.stream()
            .map { item -> item.authority }
            .collect(Collectors.toList())
    }

}
