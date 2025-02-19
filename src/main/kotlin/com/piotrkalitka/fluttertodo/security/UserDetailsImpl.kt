package com.piotrkalitka.fluttertodo.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.piotrkalitka.fluttertodo.jpa.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl(
    val id: Long,
    private val username: String,
    val email: String,
    @JsonIgnore
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails{

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = user.roles.stream()
                .map { role -> SimpleGrantedAuthority(role.name.name) }
                .collect(Collectors.toList())

            return UserDetailsImpl(
                user.id!!,
                user.username,
                user.email,
                user.password,
                authorities
            )
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

}
