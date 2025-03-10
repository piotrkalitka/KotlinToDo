package com.piotrkalitka.fluttertodo.security

import com.piotrkalitka.fluttertodo.jpa.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsService @Autowired constructor(
    val userRepository: UserRepository
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException("User with username: $username could not be found") }
        return UserDetailsImpl.build(user)
    }

}
