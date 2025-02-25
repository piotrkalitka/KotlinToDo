package com.piotrkalitka.fluttertodo.controller

import com.piotrkalitka.fluttertodo.controller.dto.request.UpdateUserRequestBody
import com.piotrkalitka.fluttertodo.jpa.entity.User
import com.piotrkalitka.fluttertodo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
    val userService: UserService
) {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN)")
    fun getUserById(@PathVariable("id") userId: Long): User {
        return userService.getUserById(userId)
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateUser(@PathVariable("id") userId: Long, request: UpdateUserRequestBody): User {
        return userService.updateUser(userId, request.username, request.email, request.password, request.roles)
    }

}
