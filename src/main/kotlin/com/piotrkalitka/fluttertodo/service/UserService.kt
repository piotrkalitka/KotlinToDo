package com.piotrkalitka.fluttertodo.service

import com.piotrkalitka.fluttertodo.exception.UserNotFoundException
import com.piotrkalitka.fluttertodo.jpa.entity.ERole
import com.piotrkalitka.fluttertodo.jpa.entity.Role
import com.piotrkalitka.fluttertodo.jpa.entity.User
import com.piotrkalitka.fluttertodo.jpa.repository.RoleRepository
import com.piotrkalitka.fluttertodo.jpa.repository.UserRepository
import com.piotrkalitka.fluttertodo.security.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val roleRepository: RoleRepository
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { UserNotFoundException(userId) }
    }

    fun updateUser(
        userId: Long,
        username: String?,
        email: String?,
        password: String?,
        roles: Set<String>?
    ): User {
        val user = getUserById(userId)

        username?.let {
            user.username = username
        }

        email?.let {
            user.email = email
        }

        password?.let {
            user.password = passwordEncoder.encode(password)
        }

        roles?.let {
            val newRoles = mutableSetOf<Role>()
            roles.forEach { role ->
                val declaredRole = when (role) {
                    "ROLE_USER" -> ERole.ROLE_USER
                    "ROLE_ADMIN" -> ERole.ROLE_ADMIN
                    else -> null
                }

                if (declaredRole == null) throw RuntimeException("Could not find role: $role")

                newRoles.add(roleRepository.findByName(declaredRole).orElseThrow{ RuntimeException("Could not find role: $role") })

            }
        }

        return userRepository.save(user)
    }

    fun getAuthenticatedUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as UserDetailsImpl
        return userRepository.findById(userDetails.id).orElseThrow { RuntimeException("/TODO") }
    }

}
