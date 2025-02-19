package com.piotrkalitka.fluttertodo.jpa.repository

import com.piotrkalitka.fluttertodo.jpa.entity.ERole
import com.piotrkalitka.fluttertodo.jpa.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(role: ERole): Optional<Role>
}
