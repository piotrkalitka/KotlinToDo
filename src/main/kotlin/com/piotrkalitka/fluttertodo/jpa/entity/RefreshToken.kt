package com.piotrkalitka.fluttertodo.jpa.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: User,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val expiryDate: Instant

)
