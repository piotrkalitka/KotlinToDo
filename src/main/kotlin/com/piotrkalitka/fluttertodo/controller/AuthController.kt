package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.controller.dto.request.LoginRequest
import com.piotrkalitka.fluttertodo.controller.dto.request.RegisterRequest
import com.piotrkalitka.fluttertodo.controller.dto.response.JwtResponse
import com.piotrkalitka.fluttertodo.jpa.entity.ERole
import com.piotrkalitka.fluttertodo.jpa.entity.Role
import com.piotrkalitka.fluttertodo.jpa.entity.User
import com.piotrkalitka.fluttertodo.jpa.repository.RoleRepository
import com.piotrkalitka.fluttertodo.jpa.repository.UserRepository
import com.piotrkalitka.fluttertodo.security.JwtUtils
import com.piotrkalitka.fluttertodo.security.UserDetailsImpl
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
class AuthController(
    @Autowired val authenticationManager: AuthenticationManager,
    @Autowired val userRepository: UserRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val encoder: PasswordEncoder,
    @Autowired val jwtUtils: JwtUtils
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.stream()
            .map { item -> item.authority }
            .collect(Collectors.toList())

        return ResponseEntity.ok(
            JwtResponse(
                accessToken = jwt,
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

}
